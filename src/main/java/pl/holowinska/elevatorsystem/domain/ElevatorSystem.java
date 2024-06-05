package pl.holowinska.elevatorsystem.domain;

import pl.holowinska.elevatorsystem.domain.floororderstrategy.FloorOrderStrategy;
import pl.holowinska.elevatorsystem.domain.selectelevatorstrategy.SelectElevatorStrategy;
import pl.holowinska.elevatorsystem.port.IElevatorSystem;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElevatorSystem implements IElevatorSystem {

    private final Long numElevators;
    private final Long numFloors;
    private final Map<Long, Elevator> elevators;
    private final SelectElevatorStrategy selectElevatorStrategy;
    private final FloorOrderStrategy floorOrderStrategy;
    private ArrayDeque<PickupRequest> pickupRequestQueue = new ArrayDeque<>();

    public ElevatorSystem(Long numElevators, Long numFloors, SelectElevatorStrategy selectElevatorStrategy, FloorOrderStrategy floorOrderStrategy) {
        this.numElevators = numElevators;
        this.numFloors = numFloors;
        this.elevators = new HashMap<>();
        this.selectElevatorStrategy = selectElevatorStrategy;
        this.floorOrderStrategy = floorOrderStrategy;
        initElevators();
    }

    private void initElevators() {
        for (long i = 0; i < numElevators; i++) {
            elevators.put(i, new Elevator(i));
        }
    }

    @Override
    public Map<Long, ElevatorStatusResponse> getStatus() {
        return elevators.values().stream()
                .map(Elevator::getStatus)
                .collect(Collectors.toMap(ElevatorStatusResponse::elevatorId, elevatorStatus -> elevatorStatus));
    }

    @Override
    public Long getNumFloors() {
        return numFloors;
    }

    @Override
    public Long getNumElevators() {
        return numElevators;
    }

    @Override
    public void update(Long elevatorId, int currentFloor, int targetFloor) {
        Elevator elevator = elevators.get(elevatorId);
        elevator.update(currentFloor, targetFloor);
    }

    @Override
    public void step() {
        // execute next steps for each elevator
        for (Elevator elevator : elevators.values()) {
            elevator.performStep();
        }

        for (PickupRequest pickupRequest : pickupRequestQueue) {
            pickup(pickupRequest.floor(), pickupRequest.direction());
        }
    }

    @Override
    public void pickup(int floor, int directionId) {
        Direction direction = directionId > 0 ? Direction.UP : Direction.DOWN;
        this.pickup(floor, direction);
    }

    private void pickup(int floor, Direction direction) {
        Optional<Elevator> elevator = selectElevatorStrategy.selectElevator(floor, direction, elevators.values());
        if (elevator.isPresent()) {
            if (elevator.get().getTargetFloor() == null || elevator.get().getTargetFloor() != floor) {
                elevator.get().handlePickup(floor);
            }
        } else {
            PickupRequest pickupRequest = new PickupRequest(floor, direction);
            if (!pickupRequestQueue.contains(pickupRequest)) {
                pickupRequestQueue.add(pickupRequest);
            }
        }
    }

    @Override
    public void selectTargetFloorFromInsideTheElevator(Long elevatorId, int targetFloor) {
        Elevator elevator = elevators.get(elevatorId);
        if (elevator.getCurrentFloor() != targetFloor
                && (Direction.NONE.equals(elevator.getElevatorStatus())
                || (Direction.UP.equals(elevator.getElevatorStatus()) && elevator.getCurrentFloor() < targetFloor)
                || (Direction.DOWN.equals(elevator.getElevatorStatus()) && elevator.getCurrentFloor() > targetFloor)
        )) {
            Optional<Integer> index =
                    floorOrderStrategy.selectFloorOrderPosition(elevator.getFloorsToVisit(), elevator.getElevatorStatus(), elevator.getTargetFloor(), targetFloor);
            if (index.isPresent()) {
                elevator.addTargetFloorToListAtIndex(index.get(), targetFloor);
            } else if (floorOrderStrategy.updateTargetFloorIsNecessary()) {
                elevator.addTargetFloorToListAtIndex(0, elevator.getTargetFloor());
                elevator.setTargetFloor(targetFloor);
            }
        }
    }
}
