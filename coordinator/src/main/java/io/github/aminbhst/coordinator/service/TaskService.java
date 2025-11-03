package io.github.aminbhst.coordinator.service;

import io.github.aminbhst.common.persistence.entity.Task;
import io.github.aminbhst.common.persistence.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void assignPendingTasks() {
        var tasks = taskRepository.findNextPendingTask(PageRequest.of(0, 50));
        for (Task task : tasks) {

        }
    }

}
