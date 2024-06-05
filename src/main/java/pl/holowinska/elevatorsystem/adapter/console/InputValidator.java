package pl.holowinska.elevatorsystem.adapter.console;

public class InputValidator {

    public static boolean isValidElevatorId(Long elevatorId, int numElevators) {
        return elevatorId >= 0 && elevatorId < numElevators;
    }

    public static boolean isValidFloor(int floor, Long numFloors) {
        return floor >= 0 && floor <= numFloors;
    }

    public static boolean isValidDirection(int direction) {
        return direction == -1 || direction == 1;
    }
}
