package com.be.jellyletter.dto.oauth2Dto;

import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.User;
import lombok.Data;

@Data
public class TokenResDto {

    private Role grantType;
    private String accessToken;
    private String refreshToken;
    private User user;
}
