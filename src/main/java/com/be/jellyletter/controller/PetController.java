package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.PetReqDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.service.PetService;
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
    public ResponseEntity<PetResDto> createPet(@Valid @RequestBody PetReqDto petReqDto) {
        PetResDto responseDto = petService.createPet(petReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
