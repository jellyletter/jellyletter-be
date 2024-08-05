package com.be.jellyletter.service;

import com.be.jellyletter.dto.oauth2Dto.TokenDto;
import com.be.jellyletter.jwt.JwtTokenProvider;
import com.be.jellyletter.model.RefreshToken;
import com.be.jellyletter.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(TokenDto tokenDto) {
        RefreshToken refreshToken = tokenDto.dtoToEntity();

        String userId = refreshToken.getKeyUserId();
        if(refreshTokenRepository.existsByKeyUserId(userId)){
            refreshTokenRepository.deleteByKeyUserId(userId);
        }
        refreshTokenRepository.save(refreshToken);
    }
}
