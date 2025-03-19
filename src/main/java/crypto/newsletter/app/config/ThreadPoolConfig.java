package crypto.newsletter.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig implements SchedulingConfigurer {

    private static final int SCHEDULED_TASKS = 2;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executor());
    }

    @Bean
    public Executor executor() {
        return Executors.newScheduledThreadPool(SCHEDULED_TASKS);
    }
}
