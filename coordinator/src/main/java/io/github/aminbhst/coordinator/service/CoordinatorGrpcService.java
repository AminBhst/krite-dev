package io.github.aminbhst.coordinator.service;

import io.github.aminbhst.coordinator.executor.ExecutorRegistry;
import io.github.aminbhst.coordinator.CoordinatorGrpc;
import io.github.aminbhst.coordinator.CoordinatorProto;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorGrpcService extends CoordinatorGrpc.CoordinatorImplBase {

    private final ExecutorRegistry executorRegistry;

    public CoordinatorGrpcService(ExecutorRegistry executorRegistry) {
        this.executorRegistry = executorRegistry;
    }

    @Override
    public void registerExecutor(CoordinatorProto.ExecutorInfo request, StreamObserver<CoordinatorProto.RegistrationResponse> responseObserver) {
        executorRegistry.addExecutor(request);
        var response = CoordinatorProto.RegistrationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Executor registered successfully: " + request.getExecutorId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendHeartbeat(CoordinatorProto.ExecutorHeartbeat request, StreamObserver<CoordinatorProto.HeartbeatResponse> responseObserver) {
        super.sendHeartbeat(request, responseObserver);
    }

    @Override
    public void pollTasks(CoordinatorProto.TaskPollRequest request, StreamObserver<CoordinatorProto.TaskBatch> responseObserver) {
        int maxTasks = Math.max(1, request.getMaxTasks());
        var tasks = executorRegistry.pollTasks(request.getExecutorId(), maxTasks);

        var batch = CoordinatorProto.TaskBatch.newBuilder()
                .addAllTasks(tasks)
                .build();

        responseObserver.onNext(batch);
        responseObserver.onCompleted();
    }

}
