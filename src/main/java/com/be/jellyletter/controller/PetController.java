package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.PetReqDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.service.PetService;
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
@RequestMapping("/pet")
@Validated
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @Operation(summary = "반려동물 생성 API", description = "편지 생성 전 반려동물 정보를 저장합니다. 추후 반려동물 정보는 고정한 채 답장 보내기 기능이 추가될 것을 고려하여 반려동물 정보 저장과 클로바 X 편지 생성 기능을 분리하였습니다.")
    public ResponseEntity<PetResDto> createPet(@Valid @RequestBody PetReqDto petReqDto) {
        PetResDto responseDto = petService.createPet(petReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
