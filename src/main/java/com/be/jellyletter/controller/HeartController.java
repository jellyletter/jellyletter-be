package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.HeartReqDto;
import com.be.jellyletter.dto.responseDto.HeartResDto;
import com.be.jellyletter.service.HeartService;
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
@RequestMapping("/heart")
@Validated
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    public ResponseEntity<HeartResDto> createHeart(@Valid @RequestBody HeartReqDto heartReqDto) {
        HeartResDto responseDto = heartService.createHeart(heartReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
