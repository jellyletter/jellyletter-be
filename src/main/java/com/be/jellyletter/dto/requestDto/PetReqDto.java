package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PetReqDto {

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "반려동물 이름", example = "몽실이")
    private String name;

    @NotNull(message = "Species cannot be null")
    @Schema(description = "반려동물 종", example = "CAT / DOG")
    private Species species;

    @NotBlank(message = "Owner nickname cannot be blank")
    @Schema(description = "주인을 부르는 호칭", example = "언니")
    private String ownerNickname;

    @Schema(description = "추가 정보(옵션)", example = "언니")
    private String extraDesc;

    @Schema(description = "선택형 + 1:N 매칭 정보(성격)")
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
