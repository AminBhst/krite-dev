package io.github.aminbhst.executor.runner;

import io.github.aminbhst.coordinator.CoordinatorProto;

public interface TaskRunner extends Runnable {
    String getTaskType();

    TaskRunner withTask(CoordinatorProto.TaskAssignment assignment);
}
