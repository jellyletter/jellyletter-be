package com.be.jellyletter.controller;

import com.be.jellyletter.auth.jwt.JwtService;
import com.be.jellyletter.auth.dto.NaverTokenDto;
import com.be.jellyletter.auth.dto.TokenResDto;
import com.be.jellyletter.dto.requestDto.UserPetReqDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.auth.NaverService;
import com.be.jellyletter.service.UserPetService;
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

    private final NaverService naverService;
    private final JwtService jwtService;
    private final UserPetService userPetService;

    // 네이버 소셜 로그인, 회원가입
    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<TokenResDto> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) throws IOException {
        // 프론트 측에서 넘어온 인가 코드로 카카오 네이버 서버에서 토큰 발급 받기
        NaverTokenDto naverTokenDto = naverService.getNaverToken(code, state);
        // 발급받은 토큰으로 실제 유저 정보 조회 > JWT 토큰 생성
        TokenResDto responseDto = naverService.loginWithNaver(naverTokenDto);

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
