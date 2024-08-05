package com.be.jellyletter.controller;

import com.be.jellyletter.dto.oauth2Dto.CustomUserDetails;
import com.be.jellyletter.dto.requestDto.HumanLetterReqDto;
import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.requestDto.PetIdReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.model.User;
import com.be.jellyletter.model.UserPet;
import com.be.jellyletter.service.ClovaStudioService;
import com.be.jellyletter.service.LetterService;
import com.be.jellyletter.service.PetService;
import com.be.jellyletter.service.UserPetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/letter")
@Validated
@RequiredArgsConstructor
public class LetterController {

    private final PetService petService;
    private final LetterService letterService;
    private final UserPetService userPetService;

    private final ClovaStudioService clovaStudioService;

    @PostMapping("/pet")
    @Operation(summary = "클로바 X 편지 생성 API", description = "petId를 받아 반려동물 정보를 조회하고, 클로바 X로 편지 내용을 생성하여 저장합니다.")
    public ResponseEntity<LetterResDto> createPetLetter(@Valid @RequestBody PetIdReqDto petIdReqDto) {
        PetResDto petDto = petService.getPetById(petIdReqDto.getId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + petIdReqDto.getId());
        }

        String content = clovaStudioService.sendRequest(petDto);

        LetterReqDto letterReqDto = new LetterReqDto();
        letterReqDto.setPetResDto(petDto);
        letterReqDto.setTypeCode(0);
        letterReqDto.setContent(content);
        LetterResDto responseDto = letterService.createLetter(letterReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/human")
    @Operation(summary = "유저가 보내는 답장 편지 저장 API", description = "유저 ID, 펫 ID 로 각각 조회, 연결 여부를 확인하고 유저가 보낸 편지 내용을 저장합니다.")
    public ResponseEntity<LetterResDto> createHumanLetter(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody HumanLetterReqDto humanLetterReqDto) {
        // 유저 정보 확인
        User user = userDetails.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 펫 정보 확인
        PetResDto petDto = petService.getPetById(humanLetterReqDto.getPetId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + humanLetterReqDto.getPetId());
        }

        // 유저-펫 연결 확인
        UserPetResDto userPetResDto = userPetService.getUserPetById(user.getId(), petDto.getId());
        if (userPetResDto == null) {
            throw new IllegalArgumentException("User-Pet not found");
        }

        LetterReqDto letterReqDto = new LetterReqDto();
        letterReqDto.setPetResDto(petDto);
        letterReqDto.setTypeCode(1);
        letterReqDto.setContent(humanLetterReqDto.getContent());
        LetterResDto responseDto = letterService.createLetter(letterReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "편지 결과 페이지 조회 API", description = "편지 링크 공유할 때, 쿼리 파라미터로 petId나 letterId가 노출되지 않도록 shareKey를 사용합니다. shareKey 값으로 편지 내용과 반려동물 정보를 조회하여 반환합니다.")
    public ResponseEntity<LetterResDto> getLetterByShareKey(@RequestParam(name = "shareKey") String shareKey) {
        LetterResDto responseDto = letterService.getLetterByShareKey(shareKey);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
