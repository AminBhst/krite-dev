package io.github.aminbhst.coordinator.executor;

import io.github.aminbhst.common.core.task.TaskStatus;
import io.github.aminbhst.common.persistence.entity.Task;
import io.github.aminbhst.common.persistence.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskAssignmentJob {

    private final ExecutorRegistry executorRegistry;
    private final TaskRepository taskRepository;

    @Scheduled(fixedDelay = 5000)
    public void assignPendingTasks() {
        if (executorRegistry.isEmpty()) return;

        List<Task> unassigned = taskRepository.findNextPendingTask(Pageable.ofSize(50));
        for (Task task : unassigned) {
            String executorId = executorRegistry.pushTask(task);
            task.setStatus(TaskStatus.IN_QUEUE);
            task.setAssignedNode(executorId);
            taskRepository.save(task);
        }
    }
}
