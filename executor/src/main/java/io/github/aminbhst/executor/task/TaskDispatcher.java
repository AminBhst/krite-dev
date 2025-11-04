package io.github.aminbhst.executor.task;

import io.github.aminbhst.executor.runner.TaskRunner;
import io.github.aminbhst.executor.runner.TaskRunnerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class TaskDispatcher {

    private final ExecutorTaskQueue taskQueue;
    private final ExecutorService executorService;
    private final TaskRunnerFactory taskRunnerFactory;

    @PostConstruct
    public void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                TaskRunner task = taskRunnerFactory.create(taskQueue.pollTask());
                executorService.submit(task);
            }
        }, "dispatcher-thread");
        dispatcher.setDaemon(true);
        dispatcher.start();
    }
}
