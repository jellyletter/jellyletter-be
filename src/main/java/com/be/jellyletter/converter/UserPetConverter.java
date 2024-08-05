package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.model.UserPet;

public class UserPetConverter {

    public static UserPetResDto entityToDto(UserPet userPet) {
        UserPetResDto dto = new UserPetResDto();

        PetResDto petResDto = PetConverter.entityToDto(userPet.getPet());

        dto.setUser(userPet.getUser());
        dto.setPetResDto(petResDto);

        return dto;
    }
}
