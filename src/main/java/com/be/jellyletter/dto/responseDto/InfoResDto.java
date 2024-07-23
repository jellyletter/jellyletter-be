package com.be.jellyletter.dto.responseDto;

import lombok.Data;

@Data
public class InfoResDto {

    private String groupId;
    private String groupName;
    private Integer code;
    private String codeName;
    private Integer orders;
    private String useYn;
}
