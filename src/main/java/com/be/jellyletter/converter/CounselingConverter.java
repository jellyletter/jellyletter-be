package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.CounselingResDto;
import com.be.jellyletter.model.Counseling;

public class CounselingConverter {

    public static CounselingResDto entityToDto(Counseling counseling) {
        CounselingResDto dto = new CounselingResDto();
        dto.setId(counseling.getId());
        dto.setContent(counseling.getContent());

        return dto;
    }
}
