package com.gsg.youtubemonitor.processing;

import com.gsg.youtubemonitor.model.CountryData;
import com.gsg.youtubemonitor.model.User;
import com.gsg.youtubemonitor.service.CountryDataService;
import com.gsg.youtubemonitor.service.UserService;
import com.gsg.youtubemonitor.youtube.YoutubeIntegration;
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

    private final CountryDataService countryDataService;

    private final YoutubeIntegration youtubeIntegration;

    @Autowired
    public JobProcessor(UserService userService, CountryDataService countryDataService, YoutubeIntegration youtubeIntegration) {
        this.userService = userService;
        this.countryDataService = countryDataService;
        this.youtubeIntegration = youtubeIntegration;
    }

    @Async
    @Transactional
    public void runJob(int userId) {
        log.info("Running job for userId[{}]", userId);
        User user = userService.getUser(userId);

        updateUserCountryData(user.getId(), user.getCountryCode());

        user.updateNextJobRunTime();
        log.info("Run job for user[id={}, country={}, jobRunMinute={}, nextJobRunTime={}]",
                user.getId(),
                user.getCountryCode(),
                user.getJobRunMinute(),
                user.getNextJobRunTime());
    }

    private void updateUserCountryData(int userId, String countryCode) {
        CountryData countryData = youtubeIntegration.getUserYoutubeData(countryCode);
        countryDataService.deleteCountryDataForUser(userId);
        countryData.setOwnerUserId(userId);
        countryDataService.createCountryData(countryData);
    }
}
