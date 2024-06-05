package pl.holowinska.elevatorsystem.port;

import pl.holowinska.elevatorsystem.domain.ElevatorStatusResponse;

import java.util.Map;

public interface IElevatorSystem {

    Map<Long, ElevatorStatusResponse> getStatus();

    void step();

    void pickup(int floor, int directionId);

    void selectTargetFloorFromInsideTheElevator(Long elevatorId, int targetFloor);

    void update(Long elevatorId, int currentFloor, int targetFloor);

    Long getNumFloors();

    Long getNumElevators();
}
