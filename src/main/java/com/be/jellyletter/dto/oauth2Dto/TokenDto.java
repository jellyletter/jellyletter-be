package com.be.jellyletter.dto.oauth2Dto;

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
        TokenResDto resDto = new TokenResDto();
        resDto.setGrantType(this.grantType);
        resDto.setAccessToken(this.accessToken);
        resDto.setRefreshToken(this.refreshToken);
        resDto.setUser(user);

        return resDto;
    }
}
