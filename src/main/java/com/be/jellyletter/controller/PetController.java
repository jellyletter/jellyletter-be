package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.PetReqDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.service.FileService;
import com.be.jellyletter.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/pet")
@Validated
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @Operation(
            summary = "반려동물 생성 API",
            description = "편지 생성 전 반려동물 정보를 저장합니다."
    )
    public ResponseEntity<PetResDto> createPet(@Valid @RequestBody PetReqDto petReqDto) throws IOException {
        PetResDto responseDto = petService.createPet(petReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
