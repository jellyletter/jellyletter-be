package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.HeartResDto;
import com.be.jellyletter.model.Heart;

public class HeartConverter {
    public static HeartResDto entityToDto(Heart heart) {
        HeartResDto dto = new HeartResDto();
        dto.setId(heart.getId());
        dto.setUserPhone(heart.getUserPhone());

        return dto;
    }
}
