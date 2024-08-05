package com.be.jellyletter.controller;

import com.be.jellyletter.dto.oauth2Dto.NaverTokenDto;
import com.be.jellyletter.dto.oauth2Dto.TokenDto;
import com.be.jellyletter.dto.oauth2Dto.TokenResDto;
import com.be.jellyletter.dto.requestDto.UserPetReqDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.service.NaverService;
import com.be.jellyletter.service.UserPetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final NaverService naverService;
    private final UserPetService userPetService;

    // 네이버 소셜 로그인, 회원가입
    @GetMapping("/oauth2/naver")
    public ResponseEntity<TokenResDto> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) throws IOException {
        // 프론트 측에서 넘어온 인가 코드로 카카오 네이버 서버에서 토큰 발급 받기
        NaverTokenDto naverTokenDto = naverService.getNaverToken(code, state);
        // 발급받은 토큰으로 실제 유저 정보 조회 > JWT 토큰 생성
        TokenResDto responseDto = naverService.loginWithNaver(naverTokenDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("api/user-pet")
    @Operation(
            summary = "유저 - 반려동물 연결 API",
            description = "로그인 후, 유저 정보와 반려동물 정보를 연결하여 저장합니다."
    )
    public ResponseEntity<UserPetResDto> createUserPet(@Valid @RequestBody UserPetReqDto userPetReqDto) {
        UserPetResDto responseDto = userPetService.createUserPet(userPetReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
