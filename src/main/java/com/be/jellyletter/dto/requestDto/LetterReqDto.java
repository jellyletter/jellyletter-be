package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LetterReqDto {

    @Schema(description = "편지 보내는 반려동물 정보")
    private PetResDto petResDto;
    @Schema(description = "클로바 X 생성 편지")
    private String content;

    public Letter dtoToEntity(Pet pet) {
        return Letter.builder()
                .pet(pet)
                .content(this.content)
                .build();
    }
}
