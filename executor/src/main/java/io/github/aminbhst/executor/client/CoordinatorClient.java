package io.github.aminbhst.executor.client;

import io.github.aminbhst.coordinator.CoordinatorGrpc;
import io.github.aminbhst.coordinator.CoordinatorProto;
import io.github.aminbhst.executor.config.ExecutorConfig;
import io.github.aminbhst.executor.util.SystemMonitor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CoordinatorClient {

    private final CoordinatorGrpc.CoordinatorBlockingStub stub;

    private final SystemMonitor systemMonitor;

    private final ExecutorConfig executorConfig;

    public CoordinatorClient(
            SystemMonitor systemMonitor,
            ExecutorConfig executorConfig,
            CoordinatorGrpc.CoordinatorBlockingStub stub
    ) {
        this.systemMonitor = systemMonitor;
        this.executorConfig = executorConfig;
        this.stub = stub;
    }

    public CoordinatorProto.RegistrationResponse registerExecutor() {
        var request = CoordinatorProto.ExecutorInfo.newBuilder()
                .setNodeId(UUID.randomUUID().toString())
                .setHostname(executorConfig.getId())
                .setCpuLoad(systemMonitor.getCpuLoad())
                .setCpuCores(systemMonitor.getPhysicalCores())
                .setMemoryTotal(systemMonitor.getTotalMemory())
                .setMemoryFree(systemMonitor.getFreeMemory())
                .build();

        return stub.registerExecutor(request);
    }

    public CoordinatorProto.HeartbeatResponse sendHeartbeat() {
        var heartbeat = CoordinatorProto.ExecutorHeartbeat.newBuilder()
                .setId(executorConfig.getId())
                .setCpuLoad(systemMonitor.getCpuLoad())
                .setMemoryFree(systemMonitor.getFreeMemory())
                .build();

        return stub.sendHeartbeat(heartbeat);
    }

}
