package io.github.aminbhst.executor.task;

import io.github.aminbhst.coordinator.CoordinatorProto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class TaskDispatcher {

    private final ExecutorTaskQueue taskQueue;
    private final ExecutorService executorService;

    @PostConstruct
    public void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CoordinatorProto.TaskAssignment task = taskQueue.pollTask();
                    executorService.submit(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "dispatcher-thread");
        dispatcher.setDaemon(true);
        dispatcher.start();
    }
}
