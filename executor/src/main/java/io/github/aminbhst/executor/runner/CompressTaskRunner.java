package io.github.aminbhst.executor.runner;

import io.github.aminbhst.coordinator.CoordinatorProto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompressTaskRunner implements TaskRunner {

    private CoordinatorProto.TaskAssignment task;

    @Override
    public void run() {
        System.out.println("asdaopsdjasdpoj");
    }

    @Override
    public String getTaskType() {
        return "COMPRESS";
    }

    @Override
    public TaskRunner withTask(CoordinatorProto.TaskAssignment assignment) {
        this.task = assignment;
        return this;
    }
}
