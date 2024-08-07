package com.be.jellyletter.controller;

import com.be.jellyletter.auth.CustomUserDetails;
import com.be.jellyletter.dto.requestDto.HumanLetterReqDto;
import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.requestDto.PetIdReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.model.User;
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

import java.util.List;

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
    @Operation(summary = "반려동물이 보내는 첫번째 편지 생성 및 저장 API", description = "petId를 받아 반려동물 정보를 조회하고, 클로바 X로 편지 내용을 생성하여 저장합니다.")
    public ResponseEntity<LetterResDto> createPetFirstLetter(@Valid @RequestBody PetIdReqDto petIdReqDto) {
        // 펫 정보 조회
        PetResDto petDto = petService.getPetById(petIdReqDto.getId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + petIdReqDto.getId());
        }

        String content = clovaStudioService.generateFirstLetter(petDto);

        LetterReqDto letterReqDto = new LetterReqDto();
        letterReqDto.setPetResDto(petDto);
        letterReqDto.setTypeCode(0);
        letterReqDto.setContent(content);
        LetterResDto responseDto = letterService.createLetter(letterReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/human-reply")
    @Operation(summary = "유저가 보내는 답장 편지 저장 API", description = "유저 ID, 펫 ID 로 각각 조회, 연결 여부를 확인하고 유저가 보낸 편지 내용을 저장합니다.")
    public ResponseEntity<LetterResDto> createHumanReplyLetter(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody HumanLetterReqDto humanLetterReqDto) {
        // 유저 정보 확인
        User user = userDetails.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 펫 정보 조회
        PetResDto petDto = petService.getPetById(humanLetterReqDto.getPetId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + humanLetterReqDto.getPetId());
        }

        // 유저-펫 연결 조회
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

    @PostMapping("/pet-reply")
    @Operation(summary = "반려동물이 보내는 답장 편지 생성 및 저장 API", description = "로그인 정보로 유저 확인하고, petId로 반려동물 정보를 조회하여 클로바 X로 답장 편지를 생성하여 저장합니다.")
    public ResponseEntity<LetterResDto> createPetReplyLetter(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PetIdReqDto petIdReqDto
    ) {
        // 유저 정보 확인
        User user = userDetails.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 펫 정보 조회
        PetResDto petDto = petService.getPetById(petIdReqDto.getId());
        if (petDto == null) {
            throw new IllegalArgumentException("Pet not found for ID: " + petIdReqDto.getId());
        }

        // 유저-펫 연결 조회
        UserPetResDto userPetResDto = userPetService.getUserPetById(user.getId(), petDto.getId());
        if (userPetResDto == null) {
            throw new IllegalArgumentException("User-Pet not found");
        }

        // 유저가 마지막으로 보낸 편지(=바로 직전 편지) 조회
        LetterResDto letterDto = letterService.getLastLetterByPetIdAndTypeCode(petDto.getId(), 1);

        String replyContent = clovaStudioService.generateReplyLetter(petDto, letterDto);

        LetterReqDto letterReqDto = new LetterReqDto();
        letterReqDto.setPetResDto(petDto);
        letterReqDto.setTypeCode(0);
        letterReqDto.setContent(replyContent);
        LetterResDto responseDto = letterService.createLetter(letterReqDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "편지 결과 페이지 조회 API", description = "편지 링크 공유할 때, 쿼리 파라미터로 petId나 letterId가 노출되지 않도록 shareKey를 사용합니다. shareKey 값으로 편지 내용과 반려동물 정보를 조회하여 반환합니다.")
    public ResponseEntity<LetterResDto> getLetterByShareKey(@RequestParam(name = "shareKey") String shareKey) {
        LetterResDto responseDto = letterService.getLetterByShareKey(shareKey);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/user-pet")
    @Operation(summary = "유저 - 반려동물 간 주고 받은 편지 조회 API", description = "로그인 정보로 유저 확인하고, petId로 반려동물 정보를 조회하여 주고받은 편지 내역 전체를 반환합니다.")
    public List<LetterResDto> getAllUserPetLetters(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 유저 정보 확인
        User user = userDetails.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 유저와 반려동물 간 주고 받은 편지 내역 조회
        // 우선 유저에게 반려동물이 1 마리만 있는 상태라 유저에게 연결된 반려동물 편지 모두 조회
        List<LetterResDto> responseDto = letterService.getAllUserPetLetters(user.getId());

        return responseDto;
    }
}
