package com.be.jellyletter.dto.oauth2Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverTokenDto {

    private String accessToken;
    private String refreshToken;
}
