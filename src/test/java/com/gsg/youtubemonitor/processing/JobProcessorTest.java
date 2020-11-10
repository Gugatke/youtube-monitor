package com.gsg.youtubemonitor.processing;

import com.gsg.youtubemonitor.model.CountryData;
import com.gsg.youtubemonitor.model.User;
import com.gsg.youtubemonitor.service.CountryDataService;
import com.gsg.youtubemonitor.service.UserService;
import com.gsg.youtubemonitor.socket.EventWebSocketHandler;
import com.gsg.youtubemonitor.youtube.YoutubeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class JobProcessorTest {

    private UserService userService;

    private CountryDataService countryDataService;

    private YoutubeIntegration youtubeIntegration;

    private EventWebSocketHandler eventWebSocketHandler;
    private JobProcessor jobProcessor;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        countryDataService = mock(CountryDataService.class);
        youtubeIntegration = mock(YoutubeIntegration.class);
        eventWebSocketHandler = mock(EventWebSocketHandler.class);
        jobProcessor = new JobProcessor(userService, countryDataService, youtubeIntegration, eventWebSocketHandler);
        jobProcessor.SELF = jobProcessor;
    }

    @Test
    void testDataCrudOperationsAreCalledAndClientNotified() {
        CountryData countryData = CountryData.builder()
                .mostPopularVideoUrl("https://yoputube.com")
                .build();

        when(youtubeIntegration.getUserYoutubeData(Mockito.anyString()))
                .then(invocation -> {
                    countryData.setCountryCode(invocation.getArgument(0));
                    return countryData;
                });
        int userId = 1;
        when(userService.getUser(userId))
                .then(invocation -> User.builder()
                        .id(userId)
                        .jobRunMinute(1)
                        .nextJobRunTime(LocalDateTime.now())
                        .countryCode("GE")
                        .username("admin")
                        .build());


        jobProcessor.runJob(userId);
        verify(countryDataService, times(1)).deleteCountryDataForUser(userId);
        verify(countryDataService, times(1)).createCountryData(countryData);
        verify(eventWebSocketHandler, times(1)).sendUpdateNotification(userId);
    }
}