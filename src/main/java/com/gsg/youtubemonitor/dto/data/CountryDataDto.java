package com.gsg.youtubemonitor.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDataDto {

    private String countryCode;

    private String videoUrl;

    private String thumbnailUrl;

    private String commentUrl;
}
