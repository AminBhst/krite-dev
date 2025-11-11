package io.github.aminbhst.executor.runner;

import com.github.luben.zstd.ZstdOutputStream;
import io.github.aminbhst.common.core.task.TaskType;
import io.github.aminbhst.common.storage.StorageService;
import io.github.aminbhst.coordinator.CoordinatorProto;
import io.github.aminbhst.executor.persistence.repository.ExecutorLogRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompressTaskRunner extends TaskRunner {

    private final StorageService storageService;

    protected CompressTaskRunner(ExecutorLogRepository executorLogRepository, StorageService storageService) {
        super(executorLogRepository);
        this.storageService = storageService;
    }

    @Override
    protected void runInternal() throws Exception {
        try (InputStream input = storageService.download(super.task.getSourceFileObjectKey())) {
            var out = new ByteArrayOutputStream();
            try (ZstdOutputStream zstdOut = new ZstdOutputStream(out)) {
                input.transferTo(zstdOut);
                storageService.upload(
                        new ByteArrayInputStream(out.toByteArray()),
                        out.size(),
                        "application/octet-stream"
                );
            }
        }
    }

    @Override
    public String getTaskType() {
        return TaskType.COMPRESS.toString();
    }

    @Override
    public void withTask(CoordinatorProto.TaskAssignment assignment) {
        this.task = assignment;
    }
}
