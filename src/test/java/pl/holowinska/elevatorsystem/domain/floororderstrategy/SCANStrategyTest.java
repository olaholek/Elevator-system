package pl.holowinska.elevatorsystem.domain.floororderstrategy;

import org.junit.jupiter.api.Test;
import pl.holowinska.elevatorsystem.domain.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SCANStrategyTest {

    private final SCANStrategy strategy = new SCANStrategy();

    @Test
    public void shouldReturnFirstPositionWhenEmptyList() {
        //given
        List<Integer> floorsToVisit = new ArrayList<>();

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.UP, null, 4);

        //then
        assertTrue(position.isPresent());
        assertEquals(0, position.get());
    }

    @Test
    public void shouldReturnCorrectPositionWhenMovingUp() {
        //given
        List<Integer> floorsToVisit = new ArrayList<>(Arrays.asList(3, 5, 7));

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.UP, 2, 4);

        //then
        assertTrue(position.isPresent());
        assertEquals(1, position.get());
    }

    @Test
    public void shouldReturnCorrectPositionWhenMovingDown() {
        //given
        List<Integer> floorsToVisit = new ArrayList<>(Arrays.asList(8, 4, 0));

        //when
        Optional<Integer> position = strategy.selectFloorOrderPosition(floorsToVisit, Direction.DOWN, 9, 2);

        //then
        assertTrue(position.isPresent());
        assertEquals(2, position.get());
    }
}
