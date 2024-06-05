package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FindNearestElevatorOnTheRoadStrategyTest {

    private FindNearestElevatorOnTheRoadStrategy strategy;
    private List<Elevator> elevators;

    @BeforeEach
    public void setup() {
        strategy = new FindNearestElevatorOnTheRoadStrategy();
        elevators = Arrays.asList(
                new Elevator(0L, 4, 9, Direction.UP, new ArrayList<>()),
                new Elevator(1L, 6, 3, Direction.DOWN, new ArrayList<>()),
                new Elevator(2L, 2, null, Direction.NONE, new ArrayList<>())
        );
    }

    @Test
    public void shouldReturnCorrectElevatorWithUpDirection() {
        //when
        Optional<Elevator> result = strategy.selectElevator(6, Direction.UP, elevators);

        //then
        assertTrue(result.isPresent());
        assertEquals(0, result.get().getId());
    }

    @Test
    public void shouldReturnCorrectElevatorWithDownDirection() {
        //when
        Optional<Elevator> result = strategy.selectElevator(5, Direction.DOWN, elevators);

        //then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void shouldNotReturnAnyElevator() {
        //when
        Optional<Elevator> result = strategy.selectElevator(3, Direction.UP, elevators);

        //then
        assertFalse(result.isPresent());
    }
}
