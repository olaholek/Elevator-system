package pl.holowinska.elevatorsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pl.holowinska.elevatorsystem.adapter.console.ConsoleController;
import pl.holowinska.elevatorsystem.domain.ElevatorSystem;

@SpringBootApplication
public class ElevatorSystemApplication implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public ElevatorSystemApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(ElevatorSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ElevatorSystem elevatorSystem = applicationContext.getBean(ElevatorSystem.class);
        ConsoleController consoleController = new ConsoleController(elevatorSystem);
        consoleController.run();
    }
}
