package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import org.junit.jupiter.api.Test;
import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FindNearestFreeElevatorStrategyTest {

    private final FindNearestFreeElevatorStrategy strategy = new FindNearestFreeElevatorStrategy();

    @Test
    public void shouldReturnCorrectElevator() {
        //given
        List<Elevator> elevators = Arrays.asList(
                new Elevator(0L, 3, 7, Direction.UP, new ArrayList<>()),
                new Elevator(1L, 5, null, Direction.NONE, new ArrayList<>()),
                new Elevator(2L, 8, null, Direction.NONE, new ArrayList<>())
        );

        //when
        Optional<Elevator> result = strategy.selectElevator(7, Direction.UP, elevators);

        //then
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getId());
    }


    @Test
    public void shouldNotReturnAnyElevator() {
        //given
        List<Elevator> elevators = Arrays.asList(
                new Elevator(0L, 3, 7, Direction.UP, new ArrayList<>()),
                new Elevator(1L, 5, 1, Direction.DOWN, new ArrayList<>()),
                new Elevator(2L, 8, 2, Direction.DOWN, new ArrayList<>())
        );

        //when
        Optional<Elevator> result = strategy.selectElevator(4, Direction.UP, elevators);

        //then
        assertFalse(result.isPresent());
    }

}
