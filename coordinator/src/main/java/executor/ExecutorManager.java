package executor;

import io.github.aminbhst.coordinator.CoordinatorProto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutorManager {

    private static final Map<String, CoordinatorProto.ExecutorInfo> executors = new ConcurrentHashMap<>();

    private static List<String> executorRanking = new ArrayList<>();

    public void addExecutor(CoordinatorProto.ExecutorInfo executor) {
        executors.put(executor.getNodeId(), executor);
    }


}
