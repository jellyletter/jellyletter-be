package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;

public class LetterConverter {

    public static LetterResDto entityToDto(Letter letter) {
        PetResDto petDto = PetConverter.entityToDto(letter.getPet());
        LetterResDto dto = new LetterResDto();
        dto.setPetResDto(petDto);
        dto.setContent(letter.getContent());
        dto.setShareKey(letter.getShareKey());

        return dto;
    }
}
