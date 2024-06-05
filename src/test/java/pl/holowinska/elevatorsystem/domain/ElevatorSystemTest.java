package pl.holowinska.elevatorsystem.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.holowinska.elevatorsystem.domain.floororderstrategy.FloorOrderStrategy;
import pl.holowinska.elevatorsystem.domain.floororderstrategy.SCANStrategy;
import pl.holowinska.elevatorsystem.domain.selectelevatorstrategy.FindOnTheRoadOrFreeStrategy;
import pl.holowinska.elevatorsystem.domain.selectelevatorstrategy.SelectElevatorStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;

class ElevatorSystemTest {
    SelectElevatorStrategy selectElevatorStrategy = Mockito.spy(new FindOnTheRoadOrFreeStrategy());
    FloorOrderStrategy floorOrderStrategy = Mockito.spy(new SCANStrategy());


    @Test
    @DisplayName("Test that elevator will not change status when is not requested after step simulation.")
    public void shouldNotChangeTheFlor() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(2L, 10L, selectElevatorStrategy, floorOrderStrategy);
        Map<Long, ElevatorStatusResponse> statusBefore = elevatorSystem.getStatus();

        //when
        elevatorSystem.step();
        elevatorSystem.step();
        elevatorSystem.step();

        Map<Long, ElevatorStatusResponse> statusAfter = elevatorSystem.getStatus();
        //then
        Assertions.assertEquals(statusBefore.size(), statusAfter.size());
        assertTrue(statusBefore.values().containsAll(statusAfter.values()));
    }

    @Test
    @DisplayName("Test that elevator will start to move in next step after pickup event.")
    public void shouldChangeTheFloorWhenRequested() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(1L, 10L, selectElevatorStrategy, floorOrderStrategy);
        AtomicReference<Elevator> reference = new AtomicReference<>();
        doAnswer(invocation -> {
            Optional<Elevator> elevator = (Optional<Elevator>) invocation.callRealMethod();
            if (elevator.isPresent()) {
                reference.set(elevator.get());
            } else {
                reference.set(null);
            }
            return elevator;
        }).when(selectElevatorStrategy).selectElevator(anyInt(), any(), any());


        //when
        elevatorSystem.pickup(5, -1);
        ElevatorStatusResponse statusBefore = reference.get().getStatus();
        assertEquals(0, statusBefore.currentFloor()); // elevator has not been moved before step
        elevatorSystem.step();
        ElevatorStatusResponse statusAfter = reference.get().getStatus();
        assertEquals(1, statusAfter.currentFloor());
    }

    @Test
    @DisplayName("""
            Test that first pickup request should be dispatched to first elevator and the elevator is in different direction
            then the secord request which should be dispatched to second elevator
            """)
    public void shouldDispatchRequestBetweenTwoElevators() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(2L, 10L, selectElevatorStrategy, floorOrderStrategy);
        AtomicReference<Elevator> reference = new AtomicReference<>();
        doAnswer(invocation -> {
            Optional<Elevator> elevator = (Optional<Elevator>) invocation.callRealMethod();
            if (elevator.isPresent()) {
                reference.set(elevator.get());
            } else {
                reference.set(null);
            }
            return elevator;
        }).when(selectElevatorStrategy).selectElevator(anyInt(), any(), any());
        Elevator firstElevator;
        Elevator secondElevator;

        //when
        elevatorSystem.pickup(5, -1); // elevator should go 0 -> 5 -> 0
        elevatorSystem.step();
        elevatorSystem.step();
        firstElevator = reference.get();
        assertEquals(2, firstElevator.getStatus().currentFloor());

        //first elevator is on the second floor and is going up. should not pick up this request
        elevatorSystem.pickup(1, -1);
        secondElevator = reference.get();

        assertNotEquals(firstElevator.getId(), secondElevator.getId());

    }

    @Test
    @DisplayName("""
            Test that first pickup request should be dispatched to first elevator but second should be queued cause of 
            lack of free elevators
            """)
    public void pickupShouldBeQueued() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(1L, 10L, selectElevatorStrategy, floorOrderStrategy);
        AtomicReference<Elevator> reference = new AtomicReference<>();
        doAnswer(invocation -> {
            Optional<Elevator> elevator = (Optional<Elevator>) invocation.callRealMethod();
            if (elevator.isPresent()) {
                reference.set(elevator.get());
            } else {
                reference.set(null);
            }
            return elevator;
        }).when(selectElevatorStrategy).selectElevator(anyInt(), any(), any());
        Elevator firstElevator;
        Elevator secondElevator;

        //when
        elevatorSystem.pickup(5, -1);
        elevatorSystem.step();
        firstElevator = reference.get();

        //if there is only one elevator the request should be queued
        elevatorSystem.pickup(1, -1);

        // should be null because of strategy returns empty optional when there is no available
        // elevator and request should be queued
        secondElevator = reference.get();

        assertNotNull(firstElevator);
        assertNull(secondElevator);
    }

    @Test
    @DisplayName("""
            Test that pickup method invoked when elevator is has requested pickup on the road then should reuse this
            elevator to make others elevator more accessible for other requests. (depends on strategy)
            """)
    public void shouldStopWhenPickupIsOnTheRoad() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(2L, 10L, selectElevatorStrategy, floorOrderStrategy);
        AtomicReference<Elevator> reference = new AtomicReference<>();
        doAnswer(invocation -> {
            Optional<Elevator> elevator = (Optional<Elevator>) invocation.callRealMethod();
            if (elevator.isPresent()) {
                reference.set(elevator.get());
            } else {
                reference.set(null);
            }
            return elevator;
        }).when(selectElevatorStrategy).selectElevator(anyInt(), any(), any());
        Elevator firstElevator;
        Elevator secondElevator;

        //when
        elevatorSystem.pickup(5, -1);
        firstElevator = reference.get();
        elevatorSystem.step();
        elevatorSystem.step();
        elevatorSystem.step();
        elevatorSystem.step();
        elevatorSystem.step();
        //if elevator reached the requested floor then user can select floor (user is inside elevator so know exact elevator - id is present),
        // 5 -> 0
        assertNull(firstElevator.getTargetFloor());
        elevatorSystem.selectTargetFloorFromInsideTheElevator(firstElevator.getId(), 0);
        elevatorSystem.step();
        assertEquals(0, firstElevator.getTargetFloor());
        // pickup on 1st floor should use the same elevator
        elevatorSystem.pickup(1, -1);
        secondElevator = reference.get();
        assertEquals(firstElevator.getId(), secondElevator.getId());

//        //if there is only one elevator the request should be queued
//        elevatorSystem.pickup(1, -1);
//
//        // should be null because of strategy returns empty optional when there is no available
//        // elevator and request should be queued
//        secondElevator = reference.get();
//
//        assertNotNull(firstElevator);
//        assertNull(secondElevator);
    }

}