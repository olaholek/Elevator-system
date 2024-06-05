package pl.holowinska.elevatorsystem.domain.selectelevatorstrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.holowinska.elevatorsystem.domain.Direction;
import pl.holowinska.elevatorsystem.domain.Elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindOnTheRoadOrFreeStrategyTest {

    @Mock
    private FindNearestElevatorOnTheRoadStrategy findNearestElevatorOnTheRoadStrategy;

    @Mock
    private FindNearestFreeElevatorStrategy findNearestFreeElevatorStrategy;

    @InjectMocks
    private FindOnTheRoadOrFreeStrategy findOnTheRoadOrFreeStrategy;

    @Test
    public void shouldReturnElevatorOnTheRoadWhenAvailable() {
        //given
        List<Elevator> elevators = List.of(
                new Elevator(0L, 2, 8, Direction.UP, new ArrayList<>()));

        //when
        when(findNearestElevatorOnTheRoadStrategy.selectElevator(anyInt(), any(), any()))
                .thenReturn(Optional.of(elevators.get(0)));

        Optional<Elevator> elevator = findOnTheRoadOrFreeStrategy.selectElevator(1, Direction.UP, elevators);

        //then
        assertEquals(elevators.get(0), elevator.get());
        verify(findNearestElevatorOnTheRoadStrategy, times(1)).selectElevator(any(), any(), any());
        verify(findNearestFreeElevatorStrategy, times(0)).selectElevator(any(), any(), any());

    }

    @Test
    public void shouldReturnNearestFreeElevatorWhenNoOnTheRoadIsAvailable() {
        //given
        List<Elevator> elevators = List.of(
                new Elevator(0L, 2, 8, Direction.NONE, new ArrayList<>()));

        //when
        when(findNearestElevatorOnTheRoadStrategy.selectElevator(anyInt(), any(), any()))
                .thenReturn(Optional.empty());
        when(findNearestFreeElevatorStrategy.selectElevator(anyInt(), any(), any()))
                .thenReturn(Optional.of(elevators.get(0)));

        Optional<Elevator> elevator = findOnTheRoadOrFreeStrategy.selectElevator(1, Direction.UP, elevators);

        //then
        assertEquals(elevators.get(0), elevator.get());
        verify(findNearestElevatorOnTheRoadStrategy, times(1)).selectElevator(any(), any(), any());
        verify(findNearestFreeElevatorStrategy, times(1)).selectElevator(any(), any(), any());
    }

    @Test
    public void shouldReturnEmptyWhenNoElevatorIsAvailable() {
        //given
        List<Elevator> elevators = List.of(
                new Elevator(0L, 2, 8, Direction.NONE, new ArrayList<>()));

        //when
        when(findNearestElevatorOnTheRoadStrategy.selectElevator(anyInt(), any(), any()))
                .thenReturn(Optional.empty());
        when(findNearestFreeElevatorStrategy.selectElevator(anyInt(), any(), any()))
                .thenReturn(Optional.empty());

        Optional<Elevator> elevator = findOnTheRoadOrFreeStrategy.selectElevator(1, Direction.UP, elevators);

        //then
        assertTrue(elevator.isEmpty());
        verify(findNearestElevatorOnTheRoadStrategy, times(1)).selectElevator(any(), any(), any());
        verify(findNearestFreeElevatorStrategy, times(1)).selectElevator(any(), any(), any());
    }
}
