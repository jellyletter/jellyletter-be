package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.PetInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetInfoReqDto {

    @NotBlank(message = "GroupId cannot be black")
    private String groupId;

    @NotNull(message = "code cannot be null")
    private Integer code;

    public PetInfo dtoToEntity() {
        return PetInfo.builder()
                .groupId(this.groupId)
                .code(this.code)
                .build();
    }
}
