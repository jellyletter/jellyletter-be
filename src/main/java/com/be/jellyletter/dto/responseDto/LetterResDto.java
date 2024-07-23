package com.be.jellyletter.dto.responseDto;

import lombok.Data;

@Data
public class LetterResDto {

    private PetResDto petResDto;
    private String content;
    private String shareKey;
}
