package pl.holowinska.elevatorsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.holowinska.elevatorsystem.domain.ElevatorSystem;
import pl.holowinska.elevatorsystem.domain.floororderstrategy.FloorOrderStrategy;
import pl.holowinska.elevatorsystem.domain.floororderstrategy.SCANStrategy;
import pl.holowinska.elevatorsystem.domain.selectelevatorstrategy.FindOnTheRoadOrFreeStrategy;
import pl.holowinska.elevatorsystem.domain.selectelevatorstrategy.SelectElevatorStrategy;

@Configuration
public class ElevatorSystemConfiguration {

    @Value("${elevator.system.numElevators}")
    private Long numElevators;

    @Value("${elevator.system.numFloors}")

    private Long numFloors;

    @Bean
    public ElevatorSystem elevatorSystem() {
        SelectElevatorStrategy elevatorStrategy = new FindOnTheRoadOrFreeStrategy();
        FloorOrderStrategy floorOrderStrategy = new SCANStrategy();
        return new ElevatorSystem(numElevators, numFloors, elevatorStrategy, floorOrderStrategy);
    }
}
