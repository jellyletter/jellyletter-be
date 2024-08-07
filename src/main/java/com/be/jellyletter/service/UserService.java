package com.be.jellyletter.service;

import com.be.jellyletter.auth.dto.TokenDto;
import com.be.jellyletter.auth.dto.TokenResDto;
import com.be.jellyletter.auth.jwt.JwtService;
import com.be.jellyletter.auth.jwt.JwtTokenProvider;
import com.be.jellyletter.dto.requestDto.LoginReqDto;
import com.be.jellyletter.enums.Oauth2Vendor;
import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.User;
import com.be.jellyletter.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    public TokenResDto login(LoginReqDto loginReqDto) {

        Oauth2Vendor vendor = null;

        if (loginReqDto.getProvider().equals("naver")) {
            vendor = Oauth2Vendor.NAVER;
        }

        if (!userRepository.existsByEmail(loginReqDto.getEmail())) {
            // DB에 해당 이메일 없을 경우 회원 가입 로직 실행
            User user = User.builder()
                    .username(loginReqDto.getName())
                    .userRole(Role.USER)
                    .email(loginReqDto.getEmail())
                    .vendor(vendor)
                    .userStatus(0)
                    .build();

            if (loginReqDto.getMobile() != null) {
                user.addUserPhone(loginReqDto.getMobile());
            }

            User savedUser = userRepository.save(user);

            TokenDto tokenDto = jwtTokenProvider.createToken(savedUser.getId(), user.getUserRole());
            tokenDto.setGrantType(user.getUserRole());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto.convertToResDto(user);
        } else {
            // DB에 해당 이메일 회원 정보 있을 경우 jwt token 생성해서 리턴
            Optional<User> user = userRepository.findByEmail(loginReqDto.getEmail());

            TokenDto tokenDto = jwtTokenProvider.createToken(user.get().getId(), user.get().getUserRole());
            tokenDto.setGrantType(user.get().getUserRole());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto.convertToResDto(user.get());
        }

    }
}
