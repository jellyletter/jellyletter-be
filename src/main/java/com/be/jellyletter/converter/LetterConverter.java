package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetAiImageResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;

public class LetterConverter {

    public static LetterResDto entityToDto(Letter letter) {
        PetResDto petDto = PetConverter.entityToDto(letter.getPet());
        PetAiImageResDto petImgDto = PetAiImageConverter.entityToDto(letter.getPetAiImage());

        LetterResDto dto = new LetterResDto();
        dto.setPetResDto(petDto);
        dto.setTypeCode(letter.getTypeCode());
        dto.setContent(letter.getContent());
        dto.setPetAiImage(petImgDto);
        dto.setShareKey(letter.getShareKey());
        dto.setCreateAt(letter.getCreatedAt());

        return dto;
    }
}
