package io.github.aminbhst.coordinator.executor;

import io.github.aminbhst.coordinator.CoordinatorProto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutorRegistry {

    private static final Map<String, CoordinatorProto.ExecutorInfo> executors = new ConcurrentHashMap<>();

    private static List<String> executorRanking = new ArrayList<>();

    public void addExecutor(CoordinatorProto.ExecutorInfo executor) {
        executors.put(executor.getNodeId(), executor);
    }

    public static void rankExecutors() {
        int maxCores = executors.values()
                .stream()
                .mapToInt(CoordinatorProto.ExecutorInfo::getCpuCores)
                .max()
                .orElse(1);

        long maxMemory = executors.values()
                .stream()
                .mapToLong(CoordinatorProto.ExecutorInfo::getMemoryTotal)
                .max()
                .orElse(1);

        double wCores = 0.3;
        double wMemory = 0.4;
        double wCpuLoad = 0.3;

        Map<Double, CoordinatorProto.ExecutorInfo> scoreMap = new HashMap<>();
        for (CoordinatorProto.ExecutorInfo e : executors.values()) {
            double coresScore = (double) e.getCpuCores() / maxCores;
            double memoryScore = (double) e.getMemoryFree() / maxMemory;
            double cpuScore = 1.0 - (e.getCpuLoad() / 100.0);

            double totalScore = wCores * coresScore + wMemory * memoryScore + wCpuLoad * cpuScore;
            scoreMap.put(totalScore, e);
        }
        executorRanking.clear();
        scoreMap.keySet()
                .stream()
                .sorted(Comparator.comparingDouble(Double::doubleValue).reversed())
                .toList()
                .forEach(score -> executorRanking.add(scoreMap.get(score).getNodeId()));
    }


}
