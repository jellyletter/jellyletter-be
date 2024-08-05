package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.HeartReqDto;
import com.be.jellyletter.dto.responseDto.HeartResDto;
import com.be.jellyletter.service.HeartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/heart")
@Validated
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    @Operation(summary = "응원 하트 생성 API", description = "하트 버튼 클릭 내역을 적재합니다. 전화번호는 선택사항입니다.")
    public ResponseEntity<HeartResDto> createHeart(@Valid @RequestBody HeartReqDto heartReqDto) {
        HeartResDto responseDto = heartService.createHeart(heartReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
