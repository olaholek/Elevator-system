package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.Collection;
import java.util.Optional;

public interface SelectElevatorStrategy {
    /***
     *
     * @param floor
     * @param direction
     * @param elevators
     * @return returns elevator if is available. Empty optional means that all elevators are
     *         busy and request should be queued
     */
    Optional<Elevator> selectElevator(Integer floor, Direction direction, Collection<Elevator> elevators);
}
