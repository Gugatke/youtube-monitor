package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.data.CountryDataDto;
import com.gsg.youtubemonitor.dto.data.CountryDataHelper;
import com.gsg.youtubemonitor.model.CountryData;
import com.gsg.youtubemonitor.repository.CountryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryDataServiceBean implements CountryDataService {

    private final CountryDataRepository countryDataRepository;

    @Autowired
    public CountryDataServiceBean(CountryDataRepository countryDataRepository) {
        this.countryDataRepository = countryDataRepository;
    }

    @Override
    public void createCountryData(CountryData countryData) {
        countryDataRepository.save(countryData);
    }

    @Override
    public CountryDataDto getCountryDataDto(Integer userId) throws YMException {
        if (userId == null) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, "User id should not be null");
        }

        CountryData countryData = countryDataRepository.findByOwnerUserId(userId);
        if (countryData == null) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, String.format("No country data found for user[%d]", userId));
        }
        return CountryDataHelper.toDto(countryData);
    }

    @Override
    public void deleteCountryDataForUser(int userId) {
        countryDataRepository.deleteByOwnerUserId(userId);
    }
}
