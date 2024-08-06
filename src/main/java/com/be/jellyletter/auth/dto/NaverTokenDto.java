package com.be.jellyletter.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverTokenDto {

    private String accessToken;
    private String refreshToken;
}
