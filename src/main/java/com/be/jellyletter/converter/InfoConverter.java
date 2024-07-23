package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.InfoResDto;
import com.be.jellyletter.model.Info;

public class InfoConverter {
    public static InfoResDto entityToDto(Info info) {
        InfoResDto dto = new InfoResDto();
        dto.setGroupId(info.getId().getGroupId());
        dto.setGroupName(info.getGroupName());
        dto.setCode(info.getId().getCode());
        dto.setCodeName(info.getCodeName());
        dto.setOrders(info.getOrders());
        dto.setUseYn(info.getUseYn());

        return dto;
    }
}
