package com.be.jellyletter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetInfoDto {

    @NotBlank(message = "Group ID cannot be blank")
    private String groupId;

    @NotNull(message = "Code cannot be null")
    private Integer code;

}
