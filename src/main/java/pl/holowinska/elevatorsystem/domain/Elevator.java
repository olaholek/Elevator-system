package pl.holowinska.elevatorsystem.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.holowinska.elevatorsystem.domain.Direction.*;

@Slf4j
public class Elevator {

    private Direction elevatorStatus;
    private Long id;
    private Integer currentFloor;
    private Integer targetFloor;
    private List<Integer> floorsToVisit;

    public Elevator(Long id) {
        this.elevatorStatus = NONE;
        this.id = id;
        this.currentFloor = 0;
        this.targetFloor = null;
        this.floorsToVisit = new ArrayList<>();
    }

    public Elevator(Long id, Integer currentFloor, Integer targetFloor, Direction elevatorStatus, List<Integer> floorsToVisit) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        this.elevatorStatus = elevatorStatus;
        this.floorsToVisit = new ArrayList<>(floorsToVisit);
    }

    public void performStep() {
        // if elevator should not move then end processing for this elevator
        if (Objects.equals(currentFloor, targetFloor)) {
            return;
        }
        if (targetFloor == null) {
            if (floorsToVisit.isEmpty()) {
                return;
            }
            targetFloor = floorsToVisit.get(0);
            if (currentFloor > targetFloor) {
                this.elevatorStatus = DOWN;
            } else {
                this.elevatorStatus = UP;
            }
        }
        if (currentFloor > targetFloor) {
            moveDown();
        } else {
            moveUp();
        }
        if (Objects.equals(currentFloor, targetFloor)) {
            targetFloor = null;
            elevatorStatus = NONE;
            if (!floorsToVisit.isEmpty()) {
                targetFloor = floorsToVisit.remove(0);
            }
        }
    }

    public ElevatorStatusResponse getStatus() {
        return new ElevatorStatusResponse(id, currentFloor, targetFloor);
    }

    public Direction getElevatorStatus() {
        return elevatorStatus;
    }

    public List<Integer> getFloorsToVisit() {
        return floorsToVisit;
    }

    public void moveUp() {
        currentFloor++;
    }

    public void moveDown() {
        currentFloor--;
    }

    public void handlePickup(Integer floor) {
        if (Objects.equals(currentFloor, floor)) {
            log.info("Current floor is the same as pickup floor");
            return;
        }
        if (targetFloor == null) {
            targetFloor = floor;
            if (currentFloor < targetFloor) {
                this.elevatorStatus = UP;
            } else {
                this.elevatorStatus = DOWN;
            }
            return;
        }
        floorsToVisit.add(0, targetFloor);
        targetFloor = floor;
    }

    public void update(int currentFloor, int targetFloor) {
        this.currentFloor = currentFloor;
        if (this.targetFloor != null) {
            this.floorsToVisit.add(0, this.targetFloor);
        }
        this.targetFloor = targetFloor;
        updateElevatorStatus(currentFloor, targetFloor);
    }

    private void updateElevatorStatus(int currentFloor, int targetFloor) {
        this.elevatorStatus = calculateElevatorStatus(currentFloor, targetFloor);
    }

    private Direction calculateElevatorStatus(int currentFloor, int targetFloor) {
        if (currentFloor < targetFloor) {
            return UP;
        } else if (currentFloor > targetFloor) {
            return DOWN;
        }
        return NONE;
    }

    public Integer getCurrentFloor() {
        return currentFloor;
    }

    public Integer getTargetFloor() {
        return targetFloor;
    }

    public Long getId() {
        return id;
    }

    public void addTargetFloorToListAtIndex(int index, int targetFloor) {
        floorsToVisit.add(index, targetFloor);
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
}
