package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;


import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.Collection;
import java.util.Optional;


/**
 * Strategy to select the most appropriate elevator based on current elevator requests and states.
 * Prioritizes elevators that are already on their way to the desired floor and direction and uses FindNearestElevatorOnTheRoadStrategy,
 * but if there is no elevator on the road, the nearest free elevator will be selecting using FindNearestFreeElevatorStrategy if such an elevator is available.
 */
public class FindOnTheRoadOrFreeStrategy implements SelectElevatorStrategy {
    private FindNearestElevatorOnTheRoadStrategy findNearestElevatorOnTheRoadStrategy;
    private FindNearestFreeElevatorStrategy findNearestFreeElevatorStrategy;

    public FindOnTheRoadOrFreeStrategy() {
        this.findNearestElevatorOnTheRoadStrategy = new FindNearestElevatorOnTheRoadStrategy();
        this.findNearestFreeElevatorStrategy = new FindNearestFreeElevatorStrategy();
    }


    public FindOnTheRoadOrFreeStrategy(FindNearestElevatorOnTheRoadStrategy findNearestElevatorOnTheRoadStrategy,
                                FindNearestFreeElevatorStrategy findNearestFreeElevatorStrategy) {
        this.findNearestElevatorOnTheRoadStrategy = findNearestElevatorOnTheRoadStrategy;
        this.findNearestFreeElevatorStrategy = findNearestFreeElevatorStrategy;
    }

    @Override
    public Optional<Elevator> selectElevator(Integer floor, Direction direction, Collection<Elevator> elevators) {
        Optional<Elevator> elevatorOnTheRoad = findNearestElevatorOnTheRoadStrategy.selectElevator(floor, direction, elevators);
        // if floor is on the road to don't block other elevators for different users the elevator
        // should stop and take passengers
        if (elevatorOnTheRoad.isPresent()) {
            return elevatorOnTheRoad;
        }

        // if there is no elevators on the road we should to search for the nearest free elevator
        return findNearestFreeElevatorStrategy.selectElevator(floor, direction, elevators);
    }
}
