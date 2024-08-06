package com.be.jellyletter.auth.dto;

import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.RefreshToken;
import com.be.jellyletter.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private Role grantType;
    private String accessToken;
    private String refreshToken;
    private String keyUserId;

    public RefreshToken dtoToEntity() {
        RefreshToken entity = RefreshToken.builder()
                .refreshToken(this.refreshToken)
                .keyUserId(this.keyUserId)
                .build();

        return entity;
    }

    public TokenResDto convertToResDto(User user) {
        return new TokenResDto(this.grantType, this.accessToken, this.refreshToken, user);
    }
}
