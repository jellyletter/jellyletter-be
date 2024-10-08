package com.be.jellyletter.dto.responseDto;

import com.be.jellyletter.model.PetAiImage;
import lombok.Data;

import java.util.Date;

@Data
public class LetterResDto {

    private PetResDto petResDto;
    private Integer typeCode;
    private String content;
    private PetAiImageResDto petAiImage;
    private String shareKey;
    private Date createAt;
}
