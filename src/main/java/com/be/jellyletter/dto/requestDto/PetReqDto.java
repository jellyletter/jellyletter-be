package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PetReqDto {

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "반려동물 이름", example = "몽실이")
    private String name;

    @NotNull(message = "Species cannot be null")
    @Schema(description = "반려동물 종", example = "CAT / DOG")
    private Species species;

    @Schema(description = "주인을 부르는 호칭(기존 옵션)", example = "언니")
    private String ownerNickname;

    @Schema(description = "주인을 부르는 호칭(사용자 입력)", example = "언니")
    private String specialOwnerNickname;

    @Schema(description = "좋아하는 장난감이나 간식", example = "츄르")
    private String toyAndTreat;

    @Schema(description = "특별한 추억", example = "왕산 마리나에 가서 놀았던 날")
    private String memory;

    @Schema(description = "성격(옵션)")
    private List<PetInfoReqDto> petInfos;

    public Pet dtoToEntity() {
        String finalOwnerNickName;

        if (this.ownerNickname != null && !this.ownerNickname.isEmpty()) {
            finalOwnerNickName = this.ownerNickname;
        } else if (this.specialOwnerNickname != null && !this.specialOwnerNickname.isEmpty()) {
            finalOwnerNickName = this.specialOwnerNickname;
        } else {
            // 두 필드 모두 값이 없는 경우 예외 처리나 기본값 설정
            throw new IllegalArgumentException("Owner nickname/Special owner nickname cannot be null");
        }

        Pet pet = Pet.builder()
                .name(this.name)
                .species(this.species)
                .ownerNickname(finalOwnerNickName)
                .toyAndTreat(this.toyAndTreat)
                .memory(this.memory)
                .build();

        if (this.petInfos != null) {
            List<PetInfo> petInfos = this.petInfos.stream().map(PetInfoReqDto::dtoToEntity).toList();
            pet.addPetInfos(petInfos);
        }

        return pet;
    }
}
