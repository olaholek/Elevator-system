package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.*;

/**
 * Strategy to select the nearest available (idle) elevator.
 */
public class FindNearestFreeElevatorStrategy implements SelectElevatorStrategy {

    @Override
    public Optional<Elevator> selectElevator(Integer floor, Direction direction, Collection<Elevator> elevators) {
        return findNearestFreeElevator(elevators, floor);
    }

    /**
     * Finds the nearest idle elevator to a specific floor.
     */
    private Optional<Elevator> findNearestFreeElevator(Collection<Elevator> elevators, Integer floor) {
        return elevators.stream()
                .filter(elevator -> Direction.NONE.equals(elevator.getElevatorStatus()))
                .map(elevator -> new AbstractMap.SimpleImmutableEntry<>(elevator, calculateDistanceForFreeElevator(elevator, floor)))
                .filter(pair -> Objects.nonNull(pair.getValue()))
                .min(Comparator.comparingInt(AbstractMap.SimpleImmutableEntry::getValue))
                .map(AbstractMap.SimpleImmutableEntry::getKey);
    }

    /**
     * Calculates the number of floors between the elevator's current floor and the requested floor.
     */
    private Integer calculateDistanceForFreeElevator(Elevator elevator, Integer floor) {
        return Math.abs(elevator.getCurrentFloor() - floor);
    }
}
