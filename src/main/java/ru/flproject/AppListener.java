package ru.flproject;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.flproject.job.JobStarter;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty("flink.submit-jobs-on-app-start")
public class AppListener {
    private final JobStarter jobStarter;

    @EventListener(ApplicationStartedEvent.class)
    @SneakyThrows
    public void onApplicationStart() {
        jobStarter.startJobs();
    }
}
