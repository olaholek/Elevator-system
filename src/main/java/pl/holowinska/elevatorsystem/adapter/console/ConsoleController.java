package pl.holowinska.elevatorsystem.adapter.console;


import pl.holowinska.elevatorsystem.port.IElevatorSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleController {

    private final IElevatorSystem elevatorSystem;
    private final Scanner scanner;

    public ConsoleController(IElevatorSystem elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("Choose what you want to do:");
            System.out.println("1. Call the elevator");
            System.out.println("2. Check status of elevators");
            System.out.println("3. Perform the simulation step");
            System.out.println("4. Update elevatorId status");
            System.out.println("5. Select target floor from inside the elevator");
            System.out.println("6. Exit");

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        handlePickup();
                        break;
                    case 2:
                        printStatus();
                        break;
                    case 3:
                        handleStep();
                        break;
                    case 4:
                        handleUpdate();
                        break;
                    case 5:
                        selectFloor();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
    }

    private void selectFloor() {
        System.out.println("Enter the elevatorId (0 to " + (elevatorSystem.getNumElevators() - 1) + "): ");
        Long elevatorId = scanner.nextLong();
        if (!InputValidator.isValidElevatorId(elevatorId, elevatorSystem.getNumElevators().intValue())) {
            System.out.println("Invalid elevatorId ID. It should be between 1 and " + elevatorSystem.getNumElevators());
            return;
        }

        System.out.println("Enter target floor: ");
        int targetFloor = scanner.nextInt();
        if (!InputValidator.isValidFloor(targetFloor, elevatorSystem.getNumFloors())) {
            System.out.println("Invalid floor number. It should be between 0 and " + elevatorSystem.getNumFloors());
            return;
        }

        elevatorSystem.selectTargetFloorFromInsideTheElevator(elevatorId, targetFloor);
    }

    private void handleUpdate() {
        System.out.println("Enter the elevatorId ID (0 to " + (elevatorSystem.getNumElevators() - 1) + "): ");
        Long elevatorId = scanner.nextLong();
        if (!InputValidator.isValidElevatorId(elevatorId, elevatorSystem.getNumElevators().intValue())) {
            System.out.println("Invalid elevatorId ID. It should be between 1 and " + elevatorSystem.getNumElevators());
            return;
        }

        System.out.println("Enter current floor: ");
        int currentFloor = scanner.nextInt();
        if (!InputValidator.isValidFloor(currentFloor, elevatorSystem.getNumFloors())) {
            System.out.println("Invalid floor number. It should be between 0 and " + elevatorSystem.getNumFloors());
            return;
        }

        System.out.println("Enter target floor: ");
        int targetFloor = scanner.nextInt();
        if (!InputValidator.isValidFloor(targetFloor, elevatorSystem.getNumFloors())) {
            System.out.println("Invalid floor number. It should be between 0 and " + elevatorSystem.getNumFloors());
            return;
        }

        elevatorSystem.update(elevatorId, currentFloor, targetFloor);
    }

    private void handleStep() {
        elevatorSystem.step();
    }

    private void printStatus() {
        System.out.println(elevatorSystem.getStatus().toString());
    }

    private void handlePickup() {
        System.out.println("Enter floor (0 to " + elevatorSystem.getNumFloors() + "): ");
        int currentFloor = scanner.nextInt();
        if (!InputValidator.isValidFloor(currentFloor, elevatorSystem.getNumFloors())) {
            System.out.println("Invalid floor number. It should be between 0 and " + elevatorSystem.getNumFloors());
            return;
        }

        System.out.println("Enter direction (1 for up, -1 for down): ");
        int direction = scanner.nextInt();
        if (!InputValidator.isValidDirection(direction)) {
            System.out.println("Invalid direction. It should be 1 for up or -1 for down.");
            return;
        }

        elevatorSystem.pickup(currentFloor, direction);
    }
}
