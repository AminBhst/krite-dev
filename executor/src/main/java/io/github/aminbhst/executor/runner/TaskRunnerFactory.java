package io.github.aminbhst.executor.runner;

import io.github.aminbhst.coordinator.CoordinatorProto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskRunnerFactory {

    private final List<TaskRunner> runners;
    private final Map<String, TaskRunner> registry = new HashMap<>();

    private ApplicationContext ctx;

    @Autowired
    public TaskRunnerFactory(List<TaskRunner> runners, ApplicationContext ctx) {
        this.runners = runners;
        this.ctx = ctx;
    }

    @PostConstruct
    void init() {
        for (TaskRunner runner : runners) {
            registry.put(runner.getTaskType(), runner);
        }
    }

    public TaskRunner create(CoordinatorProto.TaskAssignment assignment) {
        Class<? extends TaskRunner> runnerClass = registry.get(assignment.getTaskType());
        if (runnerClass == null) {
            throw new IllegalArgumentException("Unknown task type: " + assignment.getTaskType());
        }

        // Spring creates a new bean instance of that class
        TaskRunner runner = ctx.getBean(runnerClass);
        return runner.withAssignment(assignment);
    }
}
