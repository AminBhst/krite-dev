package io.github.aminbhst.executor;

import com.aminbhst.quartzautoconfigboot.annotation.EnableQuartzConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableQuartzConfiguration
@ComponentScan(basePackages = {"io.github.aminbhst.common"})
public class ExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

}
