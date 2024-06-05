# Elevator system

## Description
Elevator system is a Spring Boot application that is used to operate elevators in a building.

## Requirements
For building and running the application you need:
- Java Development Kit (JDK) 17
- Maven 3

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the 
pl.holowinska.elevatorsystem.ElevatorSystemApplication class from your IDE.
Alternatively you can use the Spring Boot Maven plugin like so:
1. Navigate to the project directory.
2. Use Maven to run the Spring Boot application:
    ``` sh
    ./mvnw spring-boot:run
    ```

## Testing
To run the tests, use:
```sh
    ./mvnw test
```

## Implementation

**ElevatorSystem** is a core class responsible for managing a set of elevators.
On the class instance, you can call methods that will change the state of individual
elevators (methods defined in the task). The class adopts different strategies as
algorithms to manage task handling (pickup) and the order of visiting floors

**SelectElevatorStrategy** is responsible for finding the most suitable elevator.
As the selection of an elevator may depend on many factors, the elevator selection
algorithm itself has been separated to allow for the greatest possible freedom in
configuration.

Three implementations have been made (each returns an empty optional if it fails to select an elevator -
such a case must be queued and wait for a free elevator):
- FindNearestElevatorStrategy - finds the nearest available elevator
- FindNearestElevatorOnTheRoad - returns the elevator for which the pickup is on the road
- FindOnTheRoadOrFree - is a combination of both of the above strategies. First, it selects an elevator that is along the way so as to minimize the wear and tear of the elevators and ensure greater accessibility for other users. If there is no elevator on the way, it selects the nearest available elevator.

**FloorOrderStrategy** is responsible for selecting the order of visited floors for the elevator. Two implementations have been created:
- FCFS - first come first served, the simplest mechanism that uses a FIFO queue
- SCAN - scanning of selected floors to make the elevator movements as optimal as possible