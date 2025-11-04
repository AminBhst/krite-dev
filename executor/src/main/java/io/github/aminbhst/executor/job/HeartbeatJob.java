package io.github.aminbhst.executor.job;

import io.github.aminbhst.executor.client.CoordinatorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeartbeatJob {

    private static final int MAX_HEARTBEAT_FAILURE = 3;
    private int heartbeatFailureCount = 0;

    private final CoordinatorClient coordinatorClient;


    @Scheduled(fixedRate = 5_000)
    public void sendHeartbeat() {
        try {
            var response = coordinatorClient.sendHeartbeat();
            System.out.println("Sent heartbeat to coordinator");
        } catch (Exception e) {
            heartbeatFailureCount++;
            System.err.println("Failed to send heartbeat: " + e.getMessage());
            if (heartbeatFailureCount >= MAX_HEARTBEAT_FAILURE) {
                System.err.println("Heartbeat failed 3 times! Shutting down the executor...");
                System.exit(1);
            }
        }
    }
}
