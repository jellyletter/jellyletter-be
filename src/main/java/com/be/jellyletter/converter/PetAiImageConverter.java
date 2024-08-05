package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.PetAiImageResDto;
import com.be.jellyletter.model.PetAiImage;

public class PetAiImageConverter {

    public static PetAiImageResDto entityToDto(PetAiImage petAiImage) {
        PetAiImageResDto dto = new PetAiImageResDto();
        dto.setId(petAiImage.getId());
        dto.setSpecies(petAiImage.getSpecies());
        dto.setImageUrl(petAiImage.getImageUrl());
        dto.setMessage(petAiImage.getMessage());

        return dto;
    }
}
