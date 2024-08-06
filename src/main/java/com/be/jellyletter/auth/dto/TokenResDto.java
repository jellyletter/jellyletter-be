package com.be.jellyletter.auth.dto;

import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResDto {

    private Role grantType;
    private String accessToken;
    private String refreshToken;
    private User user;
}
