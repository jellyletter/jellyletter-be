package com.be.jellyletter.dto.responseDto;

import com.be.jellyletter.model.PetAiImage;
import lombok.Data;

@Data
public class LetterResDto {

    private PetResDto petResDto;
    private Integer typeCode;
    private String content;
    private PetAiImage petAiImage;
    private String shareKey;
}
