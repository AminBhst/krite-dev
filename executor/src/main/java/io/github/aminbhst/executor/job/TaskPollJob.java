package io.github.aminbhst.executor.job;

import io.github.aminbhst.coordinator.CoordinatorProto;
import io.github.aminbhst.executor.client.CoordinatorClient;
import io.github.aminbhst.executor.task.ExecutorTaskQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskPollJob {

    private static final int MAX_QUEUE_TASKS = 50;

    private final ExecutorTaskQueue taskQueue;

    private final CoordinatorClient coordinatorClient;

    @Scheduled(fixedRate = 5_000)
    public void pollTasks() {
        int queueSize = taskQueue.size();
        if (queueSize >= MAX_QUEUE_TASKS) {
            return;
        }
        int maxTasks = MAX_QUEUE_TASKS - queueSize;
        CoordinatorProto.TaskBatch taskBatch = coordinatorClient.pollTask(maxTasks);
        taskBatch.getTasksList().forEach(taskQueue::pushTask);
    }

}
