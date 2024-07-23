package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.PetInfo;
import lombok.Data;

@Data
public class PetInfoReqDto {

    private String groupId;
    private Integer code;

    public PetInfo dtoToEntity() {
        return PetInfo.builder()
                .groupId(this.groupId)
                .code(this.code)
                .build();
    }
}
