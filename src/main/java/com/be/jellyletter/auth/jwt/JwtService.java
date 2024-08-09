package com.be.jellyletter.auth.jwt;

import com.be.jellyletter.auth.dto.TokenDto;
import com.be.jellyletter.model.RefreshToken;
import com.be.jellyletter.repository.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(TokenDto tokenDto) {
        RefreshToken refreshToken = tokenDto.dtoToEntity();

        String userId = refreshToken.getKeyUserId();
        if(refreshTokenRepository.existsByKeyUserId(userId)){
            refreshTokenRepository.deleteByKeyUserId(userId);
        }
        refreshTokenRepository.save(refreshToken);
    }

    public String validateRefreshToken(String refreshToken) {
        RefreshToken getRefreshToken = getRefreshToken(refreshToken);
        String createdAccessToken = jwtTokenProvider.validateRefreshToken(getRefreshToken);

        if (createdAccessToken == null) {
            throw new JwtAuthenticationException("TOKEN_EXPIRED");
        }

        return createdAccessToken;
    }

    private RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new JwtException("REFRESH TOKEN NOT FOUND"));
    }
}
