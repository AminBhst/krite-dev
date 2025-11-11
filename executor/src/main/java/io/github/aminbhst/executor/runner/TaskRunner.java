package io.github.aminbhst.executor.runner;

import io.github.aminbhst.coordinator.CoordinatorProto;
import io.github.aminbhst.executor.persistence.entity.ExecutorLog;
import io.github.aminbhst.executor.persistence.repository.ExecutorLogRepository;

import java.util.Map;

public abstract class TaskRunner implements Runnable {

    protected CoordinatorProto.TaskAssignment task;

    private final ExecutorLogRepository executorLogRepository;

    protected TaskRunner(ExecutorLogRepository executorLogRepository) {
        this.executorLogRepository = executorLogRepository;
    }

    abstract String getTaskType();

    abstract protected void runInternal() throws Exception;

    @Override
    public void run() {
        try {
            runInternal();
        } catch (Exception e) {
            ExecutorLog log = new ExecutorLog();
            log.setTaskId(task.getTaskId());
            log.setMessage("Failed to run task " + task.getTaskType());
            log.setException(Map.of(e.getClass().getSimpleName(), e.getMessage()));
            executorLogRepository.save(log);
            throw new RuntimeException(e);
        }
    }

    void withTask(CoordinatorProto.TaskAssignment assignment) {
        this.task = assignment;
    }
}
