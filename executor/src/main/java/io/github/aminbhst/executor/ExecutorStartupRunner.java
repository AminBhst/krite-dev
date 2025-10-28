package io.github.aminbhst.executor;

import io.github.aminbhst.executor.client.CoordinatorClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExecutorStartupRunner implements CommandLineRunner {

    private final CoordinatorClient coordinatorClient;

    public ExecutorStartupRunner(CoordinatorClient coordinatorClient) {
        this.coordinatorClient = coordinatorClient;
    }

    @Override
    public void run(String... args) {
//        try {
//            var response = coordinatorClient.registerExecutor();
//            if (!response.getSuccess()) {
//                System.out.println("Failed to register executor!"); /// TODO replace with proper logger
//                System.exit(1);
//            }
//        } catch (Exception e) {
//            System.out.println("Failed to register executor!"); /// TODO replace with proper logger
//            System.exit(1);
//        }
    }
}
