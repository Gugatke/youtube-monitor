package com.gsg.youtubemonitor.processing;

import com.gsg.youtubemonitor.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class JobsSchedulerTest {

    private UserService userService;

    private JobProcessor jobProcessor;

    private JobsScheduler scheduler;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jobProcessor = mock(JobProcessor.class);
        scheduler = new JobsScheduler(userService, jobProcessor);
    }

    @Test
    void testIdsAreDistributedCorrectly() {
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6);
        when(userService.getUserIdsToRunJobs())
                .then(invocation -> ids);
        scheduler.processJobs();
        verify(jobProcessor, times(ids.size())).runJob(anyInt());
    }
}