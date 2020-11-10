package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.data.CountryDataDto;
import com.gsg.youtubemonitor.model.CountryData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CountryDataServiceTest {

    @Autowired
    private CountryDataService countryDataService;

    @Test
    void testGetUserDataReturnsCorrectly() throws YMException {
        int ownerUserId = 1;
        String mostPopularVideoUrl = "#";
        CountryData countryData = CountryData.builder()
                                             .countryCode("GE")
                                             .mostPopularVideoUrl(mostPopularVideoUrl)
                                             .ownerUserId(ownerUserId)
                                             .build();
        countryDataService.createCountryData(countryData);
        CountryDataDto retrievedCountryData = countryDataService.getCountryDataDto(ownerUserId);
        assertEquals(countryData.getMostPopularVideoUrl(), retrievedCountryData.getVideoUrl());
    }
}