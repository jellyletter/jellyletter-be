package com.be.jellyletter.controller;

import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.requestDto.PetIdReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.service.ClovaStudioService;
import com.be.jellyletter.service.LetterService;
import com.be.jellyletter.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
@Validated
@RequiredArgsConstructor
public class LetterController {

    private final PetService petService;
    private final LetterService letterService;
    private final ClovaStudioService clovaStudioService;

    @PostMapping
    public ResponseEntity<LetterResDto> createLetter(@Valid @RequestBody PetIdReqDto petIdReqDto) {
        PetResDto petDto = petService.getPetById(petIdReqDto.getId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + petIdReqDto.getId());
        }

        String content = clovaStudioService.sendRequest(petDto);

        LetterReqDto letterReqDto = new LetterReqDto();
        letterReqDto.setPetResDto(petDto);
        letterReqDto.setContent(content);
        LetterResDto responseDto = letterService.createLetter(letterReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
