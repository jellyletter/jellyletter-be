package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PetReqDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Species cannot be null")
    private Species species;

    @NotBlank(message = "Owner nickname cannot be blank")
    private String ownerNickname;

    private String extraDesc;

    private List<PetInfoReqDto> petInfos;

    public Pet dtoToEntity() {
        Pet pet = Pet.builder()
                .name(this.name)
                .species(this.species)
                .ownerNickname(this.ownerNickname)
                .extraDesc(this.extraDesc)
                .build();

        if (this.petInfos != null) {
            List<PetInfo> petInfos = this.petInfos.stream().map(PetInfoReqDto::dtoToEntity).toList();
            pet.addPetInfos(petInfos);
        }

        return pet;
    }
}
