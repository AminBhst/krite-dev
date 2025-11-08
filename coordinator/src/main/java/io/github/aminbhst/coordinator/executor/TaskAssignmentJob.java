package io.github.aminbhst.coordinator.executor;

import io.github.aminbhst.common.core.task.TaskStatus;
import io.github.aminbhst.common.persistence.entity.Task;
import io.github.aminbhst.common.persistence.repository.TaskRepository;
import io.github.aminbhst.coordinator.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskAssignmentJob {

    private final ExecutorRegistry executorRegistry;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void assignPendingTasks() {
        if (executorRegistry.isEmpty()) return;

        List<Task> unassigned = taskService.findPendingTasks(Pageable.ofSize(50));
        for (Task task : unassigned) {
            executorRegistry.pushTask(task);
            task.setStatus(TaskStatus.IN_COORDINATOR_QUEUE);
            System.out.println("Assigning task " + task.getId() + " to coordinator");
        }
        taskRepository.saveAll(unassigned);
    }
}
