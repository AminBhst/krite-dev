package io.github.aminbhst.executor;

import com.aminbhst.quartzautoconfigboot.annotation.EnableQuartzConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableQuartzConfiguration
public class ExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

}
