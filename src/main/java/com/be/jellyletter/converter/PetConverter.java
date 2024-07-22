package com.be.jellyletter.converter;

import com.be.jellyletter.dto.PetDto;
import com.be.jellyletter.dto.PetInfoDto;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetInfo;

import java.util.List;

public class PetConverter {
    public static Pet dtoToEntity(PetDto dto) {
        Pet pet = Pet.builder()
                .name(dto.getName())
                .species(dto.getSpecies())
                .ownerNickname(dto.getOwnerNickname())
                .extraDesc(dto.getExtraDesc())
                .build();

        List<PetInfo> petInfos = dto.getPetInfos() != null ? dto.getPetInfos().stream()
                .map(dtoInfo -> PetInfo.builder()
                        .groupId(dtoInfo.getGroupId())
                        .code(dtoInfo.getCode())
                        .pet(pet) // Pass the Pet instance to PetInfo
                        .build()
                ).toList() : List.of();

        pet.addPetInfos(petInfos);

        return pet;
    }

    public static PetDto entityToDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setOwnerNickname(pet.getOwnerNickname());
        dto.setExtraDesc(pet.getExtraDesc());

        if (pet.getPetInfos() != null) {
            List<PetInfoDto> petInfoDtos = pet.getPetInfos().stream()
                    .map(petInfo -> {
                        PetInfoDto petInfoDto = new PetInfoDto();
                        petInfoDto.setGroupId(petInfo.getGroupId());
                        petInfoDto.setCode(petInfo.getCode());
                        return petInfoDto;
                    }).toList();
            dto.setPetInfos(petInfoDtos);
        }

        return dto;
    }

}
