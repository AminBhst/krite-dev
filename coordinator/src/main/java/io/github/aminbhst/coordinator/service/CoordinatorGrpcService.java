package io.github.aminbhst.coordinator.service;


import io.github.aminbhst.coordinator.CoordinatorGrpc;
import io.github.aminbhst.coordinator.CoordinatorProto;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorGrpcService extends CoordinatorGrpc.CoordinatorImplBase {
//    private final ExecutorManager executorManager;

//    public CoordinatorServiceImpl(ExecutorManager executorManager) {
//        this.executorManager = executorManager;
//    }

    @Override
    public void registerExecutor(CoordinatorProto.ExecutorInfo request, StreamObserver<CoordinatorProto.RegistrationResponse> responseObserver) {
//        executorManager.addExecutor(request);
        var response = CoordinatorProto.RegistrationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Executor registered successfully: " + request.getNodeId())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendHeartbeat(CoordinatorProto.ExecutorHeartbeat request, StreamObserver<CoordinatorProto.HeartbeatResponse> responseObserver) {
        super.sendHeartbeat(request, responseObserver);
    }

    @Override
    public void assignTask(CoordinatorProto.TaskRequest request, StreamObserver<CoordinatorProto.TaskAssignment> responseObserver) {
        super.assignTask(request, responseObserver);
    }
}
