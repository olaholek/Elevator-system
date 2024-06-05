package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.*;

/**
 * Strategy for selecting an elevator for which our floor is on its road
 * This strategy only considers elevators that are currently moving (not idle).
 */
public class FindNearestElevatorOnTheRoadStrategy implements SelectElevatorStrategy {

    @Override
    public Optional<Elevator> selectElevator(Integer floor, Direction direction, Collection<Elevator> elevators) {
        return findNearestElevatorOnTheRoad(elevators, floor, direction);
    }

    /**
     * Finds the nearest elevator on its way to a specific floor and direction.
     */
    private Optional<Elevator> findNearestElevatorOnTheRoad(Collection<Elevator> elevators, Integer floor, Direction direction) {
        return elevators.stream()
                .filter(elevator -> !Direction.NONE.equals(elevator.getElevatorStatus()))
                .map(elevator -> new AbstractMap.SimpleImmutableEntry<>(elevator, calculateDistanceForElevatorOnTheRoad(elevator, floor, direction)))
                .filter(pair -> Objects.nonNull(pair.getValue()))
                .min(Comparator.comparingInt(AbstractMap.SimpleImmutableEntry::getValue))
                .map(AbstractMap.SimpleImmutableEntry::getKey);
    }

    /**
     * Calculates the distance from the current elevator floor to the requested floor,
     * only considering elevators that are moving in a direction that will pass through the requested floor.
     */
    private Integer calculateDistanceForElevatorOnTheRoad(Elevator elevator, Integer floor, Direction direction) {
        if (Direction.UP.equals(direction) && elevator.getCurrentFloor() <= floor && elevator.getTargetFloor() >= floor) {
            return Math.abs(elevator.getCurrentFloor() - floor);

        } else if (Direction.DOWN.equals(direction) && elevator.getCurrentFloor() >= floor && elevator.getTargetFloor() <= floor) {
            return Math.abs(elevator.getCurrentFloor() - floor);

        } else {
            // Return null if the elevator is not moving in a direction that will reach the desired floor.
            return null;
        }
    }
}
