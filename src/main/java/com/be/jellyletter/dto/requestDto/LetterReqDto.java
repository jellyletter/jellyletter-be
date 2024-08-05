package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetAiImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LetterReqDto {

    @Schema(description = "편지 보내는/받는 반려동물 정보")
    private PetResDto petResDto;

    @Schema(description = "편지 구분, 0: 반려동물, 1: 사람")
    private Integer typeCode;

    @Schema(description = "편지 내용")
    private String content;

    public Letter dtoToEntity(Pet pet) {
        return Letter.builder()
                .pet(pet)
                .typeCode(this.typeCode)
                .content(this.content)
                .build();
    }

}
