package com.be.jellyletter.service;

import com.be.jellyletter.auth.dto.NaverUserInfo;
import com.be.jellyletter.auth.dto.OAuth2UserInfo;
import com.be.jellyletter.auth.dto.TokenDto;
import com.be.jellyletter.auth.dto.TokenResDto;
import com.be.jellyletter.auth.jwt.JwtService;
import com.be.jellyletter.auth.jwt.JwtTokenProvider;
import com.be.jellyletter.enums.Oauth2Vendor;
import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.User;
import com.be.jellyletter.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    public TokenResDto login(Map<String, Object> data, String provider) {

        OAuth2UserInfo userInfo = null;
        Oauth2Vendor vendor = null;

        if (provider.equals("naver")) {
            Map<String, Object> info = (Map<String, Object>) data.get("object");
            userInfo = new NaverUserInfo((Map<String, Object>) info.get("profile"));
            vendor = Oauth2Vendor.NAVER;
        }

        if (!userRepository.existsByEmail(userInfo.getEmail())) {
            // DB에 해당 이메일 없을 경우 회원 가입 로직 실행
            User user = User.builder()
                    .username(userInfo.getUsername())
                    .userRole(Role.USER)
                    .nickname(userInfo.getNickname())
                    .email(userInfo.getEmail())
                    .userPhone(userInfo.getUserPhone())
                    .vendor(vendor)
                    .userStatus(0)
                    .build();

            userRepository.save(user);

            TokenDto tokenDto = jwtTokenProvider.createToken(userInfo.getEmail(), user.getUserRole());
            tokenDto.setGrantType(user.getUserRole());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto.convertToResDto(user);
        } else {
            // DB에 해당 이메일 회원 정보 있을 경우 jwt token 생성해서 리턴
            Optional<User> user = userRepository.findByEmail(userInfo.getEmail());

            TokenDto tokenDto = jwtTokenProvider.createToken(userInfo.getEmail(), user.get().getUserRole());
            tokenDto.setGrantType(user.get().getUserRole());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto.convertToResDto(user.get());
        }

    }
}
