package pl.holowinska.elevatorsystem.domain.floororderstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining strategies for managing the order of floors that an elevator plans to visit.
 * Implementations of this interface decide where in the sequence of destinations a new floor request should be inserted
 * based on the current state and direction of the elevator.
 */
public interface FloorOrderStrategy {

    /**
     * Determines the optimal position in the elevator's itinerary to insert a new floor request.
     * The decision is based on the current direction of the elevator, its next target floor, and the new request's floor.
     */
    Optional<Integer> selectFloorOrderPosition(List<Integer> floorsToVisit, Direction elevatorStatus, Integer elevatorTargetFloor, int targetFloor);

    /**
     * Determines whether it is necessary to update the current target floor based on a new floor request.
     */
    boolean updateTargetFloorIsNecessary();
}
