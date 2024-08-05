package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HumanLetterReqDto {

    @Schema(description = "편지 받을 반려동물 ID")
    private Integer PetId;

    @Schema(description = "유저가 입력한 편지 내용")
    private String content;

}
