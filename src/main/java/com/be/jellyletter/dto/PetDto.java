package com.be.jellyletter.dto;

import com.be.jellyletter.enums.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PetDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Species cannot be null")
    private Species species;

    @NotBlank(message = "Owner nickname cannot be blank")
    private String ownerNickname;

    private String extraDesc;

    private List<PetInfoDto> petInfos;

}
