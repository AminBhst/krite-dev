package io.github.aminbhst.executor.runner;

import io.github.aminbhst.common.core.task.TaskType;
import io.github.aminbhst.common.storage.StorageService;
import io.github.aminbhst.executor.persistence.repository.ExecutorLogRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateThumbnailTaskRunner extends TaskRunner {

    private final StorageService storageService;

    protected GenerateThumbnailTaskRunner(ExecutorLogRepository executorLogRepository, StorageService storageService) {
        super(executorLogRepository);
        this.storageService = storageService;
    }

    @Override
    public String getTaskType() {
        return TaskType.GENERATE_THUMBNAIL.toString();
    }

    @Override
    protected void runInternal() throws Exception {
        try (InputStream input = storageService.download(super.task.getSourceFileObjectKey())) {
            BufferedImage src = ImageIO.read(input);
            BufferedImage thumb = Scalr.resize(src, 300);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(thumb, "png", out);
            storageService.upload(new ByteArrayInputStream(out.toByteArray()), out.size(), "image/png");
        }
    }

}
