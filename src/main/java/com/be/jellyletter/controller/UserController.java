package com.be.jellyletter.controller;

import com.be.jellyletter.auth.jwt.JwtService;
import com.be.jellyletter.auth.dto.TokenResDto;
import com.be.jellyletter.dto.requestDto.LoginReqDto;
import com.be.jellyletter.dto.requestDto.UserPetReqDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.service.UserPetService;
import com.be.jellyletter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserPetService userPetService;

    // 네이버 소셜 로그인, 회원가입
    @PostMapping("/login/custom")
    public ResponseEntity<TokenResDto> login(@RequestBody LoginReqDto loginReqDto) throws IOException {
        TokenResDto responseDto = userService.login(loginReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 토큰 재발급
    @PostMapping("/login/refresh-token")
    public ResponseEntity<TokenResDto> refreshToken(@RequestBody Map<String, String> refreshToken) {
        // Refresh Token 검증
        String recreatedAccessToken = jwtService.validateRefreshToken(refreshToken.get("refreshToken"));

        // Access Token 재발급
        TokenResDto responseDto = TokenResDto.builder()
                .accessToken(recreatedAccessToken)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 유저 - 반려동물 연결
    @PostMapping("/api/user-pet")
    @Operation(
            summary = "유저 - 반려동물 연결 API",
            description = "로그인 후, 유저 정보와 반려동물 정보를 연결하여 저장합니다."
    )
    public ResponseEntity<UserPetResDto> createUserPet(@Valid @RequestBody UserPetReqDto userPetReqDto) {
        UserPetResDto responseDto = userPetService.createUserPet(userPetReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
