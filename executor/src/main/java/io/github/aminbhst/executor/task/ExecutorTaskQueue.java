package io.github.aminbhst.executor.task;

import io.github.aminbhst.coordinator.CoordinatorProto;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ExecutorTaskQueue {

    private final Queue<CoordinatorProto.TaskAssignment> taskQueueAssignment = new ConcurrentLinkedQueue<>();

    public final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    public void pushTask(CoordinatorProto.TaskAssignment task) {
        System.out.println("Pushing task " + task.getTaskId() + "to executor queue");
        taskQueueAssignment.add(task);
    }

    public CoordinatorProto.TaskAssignment pollTask() {
        return taskQueueAssignment.poll();
    }

    public int size() {
        return taskQueueAssignment.size();
    }
}
