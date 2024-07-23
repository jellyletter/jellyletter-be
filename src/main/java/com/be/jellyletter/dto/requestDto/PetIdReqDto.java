package com.be.jellyletter.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetIdReqDto {

    @NotNull(message = "id cannot be null")
    private Integer id;
}
