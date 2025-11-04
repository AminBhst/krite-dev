package io.github.aminbhst.coordinator.executor;

import io.github.aminbhst.common.persistence.entity.Task;
import io.github.aminbhst.coordinator.CoordinatorProto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ExecutorRegistry {

    private final Map<String, CoordinatorProto.ExecutorInfo> executors = new ConcurrentHashMap<>();
    private final Map<String, BlockingDeque<Task>> executorTasks = new ConcurrentHashMap<>();

    // expanded list for weighted round robin
    private final List<String> executorsWeighted = new ArrayList<>();

    // keeps track of next executor to use (round-robin index)
    private final AtomicInteger rrIndex = new AtomicInteger(0);

    public void addExecutor(CoordinatorProto.ExecutorInfo executor) {
        executors.put(executor.getNodeId(), executor);
        executorTasks.computeIfAbsent(executor.getNodeId(), k -> new LinkedBlockingDeque<>());
        rebuildWeights();
    }

    public void removeExecutor(String nodeId) {
        executors.remove(nodeId);
        executorTasks.remove(nodeId);
        rebuildWeights();
    }

    /** Assigns a new task to the next executor based on weighted RR. */
    public void pushTask(Task task) {
        if (executorsWeighted.isEmpty()) {
            System.err.println("No executors available to assign task: " + task.getId());
            return;
        }

        String nextId = getNextExecutorId();
        BlockingDeque<Task> queue = executorTasks.get(nextId);
        if (queue != null) {
            queue.offer(task);
        }
    }

    /** Retrieves and removes a task from the executor’s queue. */
    public Task pollTask(String executorId) {
        BlockingDeque<Task> queue = executorTasks.get(executorId);
        return (queue != null) ? queue.poll() : null;
    }

    /** Weighted Round Robin scheduler logic. */
    private String getNextExecutorId() {
        int i = Math.abs(rrIndex.getAndIncrement() % executorsWeighted.size());
        return executorsWeighted.get(i);
    }

    /** Recomputes weights and rebuilds the expanded executor list. */
    private synchronized void rebuildWeights() {
        if (executors.isEmpty()) {
            executorsWeighted.clear();
            return;
        }

        int maxCores = executors.values().stream()
                .mapToInt(CoordinatorProto.ExecutorInfo::getCpuCores)
                .max().orElse(1);

        long maxMemory = executors.values().stream()
                .mapToLong(CoordinatorProto.ExecutorInfo::getMemoryTotal)
                .max().orElse(1);

        Map<String, Integer> weights = computeWeights(maxCores, maxMemory);

        executorsWeighted.clear();
        for (Map.Entry<String, Integer> entry : weights.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                executorsWeighted.add(entry.getKey());
            }
        }

        Collections.shuffle(executorsWeighted);
    }

    private Map<String, Integer> computeWeights(int maxCores, long maxMemory) {
        double wCores = 0.3;
        double wMemory = 0.4;
        double wCpuLoad = 0.3;

        Map<String, Integer> weights = new HashMap<>();

        for (CoordinatorProto.ExecutorInfo e : executors.values()) {
            double coresScore = (double) e.getCpuCores() / maxCores;
            double memoryScore = (double) e.getMemoryFree() / maxMemory;
            double cpuScore = 1.0 - (e.getCpuLoad() / 100.0);

            double totalScore = wCores * coresScore + wMemory * memoryScore + wCpuLoad * cpuScore;

            int weight = Math.max(1, (int) Math.round(totalScore * 10));
            weights.put(e.getNodeId(), weight);
        }
        return weights;
    }
}
