package pl.holowinska.elevatorsystem.domain.floororderstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;

import java.util.List;
import java.util.Optional;

/**
 * Implements the SCAN strategy for floor order management in an elevator system.
 * This strategy involves moving the elevator in one direction until the last request in that direction is fulfilled
 * before reversing the direction. This approach minimizes change of direction and improves efficiency.
 */
public class SCANStrategy implements FloorOrderStrategy {

    private boolean isTargetFloorToUpdate = false;


    /**
     * Determines the optimal position to insert a new floor request based on the current movement direction of the elevator.
     * Requests are inserted in a way that the elevator continues to move in the same direction to serve all requests on its path.
     */
    @Override
    public Optional<Integer> selectFloorOrderPosition(List<Integer> floorsToVisit, Direction elevatorStatus, Integer elevatorTargetFloor, int targetFloor) {
        isTargetFloorToUpdate = false;
        // Prevent duplicate floor requests
        if (floorsToVisit.contains(targetFloor)) {
            return Optional.empty();
        }

        // Handling the scenario where no floors are yet scheduled
        if (floorsToVisit.isEmpty()) {
            // If the elevator is moving up but the target is below the current target floor, update is needed
            if (Direction.UP.equals(elevatorStatus) && elevatorTargetFloor != null && elevatorTargetFloor > targetFloor) {
                isTargetFloorToUpdate = true;
                return Optional.empty();
            }
            // If moving down and the target is above the current target floor, update is needed
            else if (Direction.DOWN.equals(elevatorStatus) && elevatorTargetFloor != null && elevatorTargetFloor < targetFloor) {
                isTargetFloorToUpdate = true;
                return Optional.empty();
            }
            return Optional.of(0);
        }

        if (Direction.UP.equals(elevatorStatus)) {
            Optional<Integer> minGraterThenTarget = floorsToVisit.stream()
                    .filter(a -> a > targetFloor)
                    .min(Integer::compareTo);

            if (minGraterThenTarget.isPresent() && floorsToVisit.indexOf(minGraterThenTarget.get()) == 0
                    && elevatorTargetFloor > targetFloor) {
                isTargetFloorToUpdate = true;
                return Optional.empty();
            }

            return minGraterThenTarget.map(floorsToVisit::indexOf).or(() -> Optional.of(floorsToVisit.size()));
        } else if (Direction.DOWN.equals(elevatorStatus)) {
            Optional<Integer> maxLowerThenTarget = floorsToVisit.stream()
                    .filter(a -> a < targetFloor)
                    .max(Integer::compareTo);

            if (maxLowerThenTarget.isPresent() && floorsToVisit.indexOf(maxLowerThenTarget.get()) == 0
                    && elevatorTargetFloor < targetFloor) {
                isTargetFloorToUpdate = true;
                return Optional.empty();
            }

            return maxLowerThenTarget.map(floorsToVisit::indexOf).or(() -> Optional.of(floorsToVisit.size()));
        }

        return Optional.empty();
    }

    /**
     * Indicates whether the current target floor should be updated based on new requests and the elevator's current status.
     * This is typically required if a more efficient path can be formed based on new requests.
     */
    @Override
    public boolean updateTargetFloorIsNecessary() {
        return isTargetFloorToUpdate;
    }
}
