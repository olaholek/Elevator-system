package pl.holowinska.elevatorsystem.domain;

import java.util.List;

public record ElevatorStatusResponse(Long elevatorId, Integer currentFloor, Integer targetFloor) {
}
