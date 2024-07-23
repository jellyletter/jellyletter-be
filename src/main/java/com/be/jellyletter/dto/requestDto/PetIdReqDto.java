package com.be.jellyletter.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetIdReqDto {

    @NotNull(message = "id cannot be null")
    @Schema(description = "조회할 반려동물 id", example = "1")
    private Integer id;
}
