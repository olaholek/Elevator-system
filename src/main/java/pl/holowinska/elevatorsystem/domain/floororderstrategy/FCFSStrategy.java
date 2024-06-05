package pl.holowinska.elevatorsystem.domain.floororderstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;

import java.util.List;
import java.util.Optional;

/**
 * Implements the First-Come, First-Served (FCFS) strategy for determining the order of floors that an elevator should visit.
 * This strategy adds new floor requests to the end of the queue regardless of the elevator's current direction or position.
 */
public class FCFSStrategy implements FloorOrderStrategy {

    /**
     * Selects the position to insert a new floor request based on the FCFS principle.
     * It simply adds the floor to the end of the list if it is not already present.
     */
    @Override
    public Optional<Integer> selectFloorOrderPosition(List<Integer> floorsToVisit, Direction elevatorStatus, Integer elevatorTargetFloor, int targetFloor) {
        if(floorsToVisit.contains(targetFloor)){
            //If floor is already requested should not be added
            return Optional.empty();
        }
        return Optional.of(floorsToVisit.size());
    }

    /**
     * Indicates that updating the target floor is not necessary under this strategy,
     * as the FCFS strategy does not require changing the target floor based on new requests.
     */
    @Override
    public boolean updateTargetFloorIsNecessary() {
        return false;
    }
}
