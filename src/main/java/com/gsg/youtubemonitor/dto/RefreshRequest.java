package com.gsg.youtubemonitor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshRequest {

    private String refreshToken;
}
