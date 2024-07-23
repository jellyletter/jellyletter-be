package com.be.jellyletter.controller;

import com.be.jellyletter.dto.responseDto.CounselingResDto;
import com.be.jellyletter.service.CounselingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counseling")
@Validated
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    @GetMapping
    public ResponseEntity<CounselingResDto> getRandomCounseling() {
        CounselingResDto responseDto = counselingService.getRandomCounseling();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
