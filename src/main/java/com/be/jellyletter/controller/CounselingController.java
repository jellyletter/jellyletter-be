package com.be.jellyletter.controller;

import com.be.jellyletter.dto.responseDto.CounselingResDto;
import com.be.jellyletter.service.CounselingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/counseling")
@Validated
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    @GetMapping
    @Operation(summary = "카운슬링 멘트 랜덤 조회 API", description = "DB에 있는 카운슬링 멘트 1개를 랜덤으로 조회하여 반환합니다. 반려동물 이름과 이름에 맞는 조사 처리한 텍스트 반환합니다.")
    public ResponseEntity<CounselingResDto> getRandomCounseling(@RequestParam(name = "petId") Integer petId) {
        CounselingResDto responseDto = counselingService.getRandomCounselingWithPetName(petId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
