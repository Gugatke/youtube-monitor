package com.gsg.youtubemonitor.dto.data;

import com.gsg.youtubemonitor.model.CountryData;

public class CountryDataHelper {

    public static CountryDataDto toDto(CountryData countryData) {
        if (countryData == null) {
            return null;
        }

        return CountryDataDto.builder()
                             .countryCode(countryData.getCountryCode())
                             .videoUrl(countryData.getMostPopularVideoUrl())
                             .thumbnailUrl(countryData.getMostPopularVideoThumbnailUrl())
                             .commentUrl(countryData.getMostPopularCommentUrlOnTheVideo())
                             .build();
    }
}
