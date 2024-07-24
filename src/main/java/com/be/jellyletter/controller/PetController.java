package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.PetReqDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.service.FileService;
import com.be.jellyletter.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final FileService fileService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "반려동물 생성 API", description = "편지 생성 전 반려동물 정보를 저장합니다. 이미지는 jpeg, png, heic 만 지원합니다.")
    public ResponseEntity<PetResDto> createPet(@RequestPart(value = "petReqDto") PetReqDto petReqDto,
                                               @RequestPart(value = "petImage", required = false) MultipartFile petImage) throws IOException {

        if (petImage != null && !petImage.isEmpty()) {
            String fileUrl = fileService.uploadFiles(petImage,"pet-image");
            petReqDto.setImageUrl(fileUrl);
        }
        PetResDto responseDto = petService.createPet(petReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
