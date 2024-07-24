package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.PetInfoResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Pet;

import java.util.List;

public class PetConverter {
    public static PetResDto entityToDto(Pet pet) {
        PetResDto dto = new PetResDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setOwnerNickname(pet.getOwnerNickname());
        dto.setExtraDesc(pet.getExtraDesc());
        dto.setImageUrl(pet.getImageUrl());

        if (pet.getPetInfos() != null) {
            List<PetInfoResDto> petInfoDtos = pet.getPetInfos().stream()
                    .map(petInfo -> {
                        PetInfoResDto petInfoDto = new PetInfoResDto();
                        petInfoDto.setId(petInfo.getInfoId());
                        petInfoDto.setPetId(pet.getId());
                        petInfoDto.setGroupId(petInfo.getGroupId());
                        petInfoDto.setGroupName(petInfo.getInfo().getGroupName());
                        petInfoDto.setCode(petInfo.getCode());
                        petInfoDto.setCodeName(petInfo.getInfo().getCodeName());
                        return petInfoDto;
                    }).toList();
            dto.setPetInfos(petInfoDtos);
        }

        return dto;
    }

}
