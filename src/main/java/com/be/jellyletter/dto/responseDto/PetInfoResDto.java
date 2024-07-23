package com.be.jellyletter.dto.responseDto;

import lombok.Data;

@Data
public class PetInfoResDto {
    private Integer id;
    private Integer petId;
    private String groupId;
    private String groupName;
    private Integer code;
    private String codeName;
}
