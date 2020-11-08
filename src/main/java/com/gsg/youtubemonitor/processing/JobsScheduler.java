package com.gsg.youtubemonitor.processing;

import com.gsg.youtubemonitor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobsScheduler {

    private static final Logger log = LoggerFactory.getLogger(JobsScheduler.class);

    private final UserService userService;

    private final JobProcessor jobProcessor;

    @Autowired
    public JobsScheduler(UserService userService, JobProcessor jobProcessor) {
        this.userService = userService;
        this.jobProcessor = jobProcessor;
    }

    @Scheduled(cron = "0 * * * * *") //crone expresion will run once in a minute on zeroth second
    public void processJobs() {
        List<Integer> userIds = userService.getUserIdsToRunJobs();
        userIds.forEach(userId -> jobProcessor.runJob(userId));
        log.info("Scheduled {} jobs", userIds.size());
    }
}
