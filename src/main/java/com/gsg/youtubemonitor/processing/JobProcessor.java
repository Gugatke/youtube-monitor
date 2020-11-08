package com.gsg.youtubemonitor.processing;

import com.gsg.youtubemonitor.model.User;
import com.gsg.youtubemonitor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JobProcessor {

    private static final Logger log = LoggerFactory.getLogger(JobProcessor.class);

    private final UserService userService;

    @Autowired
    public JobProcessor(UserService userService) {
        this.userService = userService;
    }

    @Async
    @Transactional
    public void runJob(int userId) {
        log.info("Running job for userId[{}]", userId);
        User user = userService.getUser(userId);
        user.updateNextJobRunTime();
        log.info("Run job for user[id={}, country={}, jobRunMinute={}, nextJobRunTime={}]",
                user.getId(),
                user.getCountryCode(),
                user.getJobRunMinute(),
                user.getNextJobRunTime());
    }
}
