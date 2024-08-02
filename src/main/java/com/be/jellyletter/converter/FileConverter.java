package com.be.jellyletter.converter;

import com.be.jellyletter.dto.responseDto.FileResDto;
import com.be.jellyletter.model.File;

public class FileConverter {

    public static FileResDto entityToDto(File file) {
        FileResDto dto = new FileResDto();
        dto.setId(file.getFileId());
        dto.setFileName(file.getFileName());
        dto.setContentType(file.getContentType());
        dto.setFileUrl(file.getFileUrl());

        return dto;
    }
}
