package io.github.aminbhst.coordinator.service;

import io.github.aminbhst.common.core.task.TaskStatus;
import io.github.aminbhst.common.persistence.entity.Task;
import io.github.aminbhst.common.persistence.repository.TaskRepository;
import io.github.aminbhst.coordinator.executor.ExecutorRegistry;
import io.github.aminbhst.coordinator.CoordinatorGrpc;
import io.github.aminbhst.coordinator.CoordinatorProto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinatorGrpcService extends CoordinatorGrpc.CoordinatorImplBase {

    private final ExecutorRegistry executorRegistry;

    private final TaskRepository taskRepository;

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
    }

    @Override
    public void pollTasks(CoordinatorProto.TaskPollRequest request, StreamObserver<CoordinatorProto.TaskBatch> responseObserver) {
        int maxTasks = Math.max(1, request.getMaxTasks());
        var tasks = executorRegistry.pollTasks(request.getExecutorId(), maxTasks);
        var tasksBatch = tasks.stream().map(Task::toTaskAssignment).toList();
        var batch = CoordinatorProto.TaskBatch.newBuilder()
                .addAllTasks(tasksBatch)
                .build();

        for (var task : tasks) {
            task.setStatus(TaskStatus.IN_EXECUTOR_QUEUE);
            task.setAssignedNode(request.getExecutorId());
        }
        taskRepository.saveAll(tasks);

        responseObserver.onNext(batch);
        responseObserver.onCompleted();
    }

}
