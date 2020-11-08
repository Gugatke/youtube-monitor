package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.data.CountryDataDto;
import com.gsg.youtubemonitor.model.CountryData;

public interface CountryDataService {

    void createCountryData(CountryData countryData);

    CountryDataDto getCountryDataDto(Integer userId) throws YMException;

    void deleteCountryDataForUser(int userId);
}
