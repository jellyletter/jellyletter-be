package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.PetInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetInfoReqDto {

    @NotBlank(message = "GroupId cannot be black")
    @Schema(description = "선택형 문항 GroupId", example = "G0001")
    private String groupId;

    @NotNull(message = "code cannot be null")
    @Schema(description = "선택형 문항 Code", example = "1")
    private Integer code;

    public PetInfo dtoToEntity() {
        return PetInfo.builder()
                .groupId(this.groupId)
                .code(this.code)
                .build();
    }
}
