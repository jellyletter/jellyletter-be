package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import lombok.Data;

@Data
public class LetterReqDto {

    private PetResDto petResDto;
    private String content;

    public Letter dtoToEntity(Pet pet) {
        return Letter.builder()
                .pet(pet)
                .content(this.content)
                .build();
    }
}
