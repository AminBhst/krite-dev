package io.github.aminbhst.executor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Getter
//@Setter
@Configuration
@ConfigurationProperties(prefix = "executor")
public class ExecutorConfig {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
