package pl.holowinska.elevatorsystem.domain.floororderstrategy;

import org.junit.jupiter.api.Test;
import pl.holowinska.elevatorsystem.domain.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FCFSStrategyTest {

    private final FCFSStrategy strategy = new FCFSStrategy();

    @Test
    public void shouldReturnCorrectPositionForNewFloor() {
        //given
        List<Integer> floorsToVisit = List.of(1, 3, 5);

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.UP, 1, 4);

        //then
        assertTrue(position.isPresent());
        assertEquals(3, position.get());
    }

    @Test
    public void shouldNotReturnAnyPositionForExistingFloor() {
        //given
        List<Integer> floorsToVisit = List.of(1, 3, 5);

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.UP, 1, 3);

        //then
        assertTrue(position.isEmpty());
    }

    @Test
    public void shouldReturnFirstPositionWhenEmptyList() {
        //given
        List<Integer> floorsToVisit = new ArrayList<>();

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.UP, 5, 2);

        //then
        assertTrue(position.isPresent());
        assertEquals(0, position.get());
    }
}
