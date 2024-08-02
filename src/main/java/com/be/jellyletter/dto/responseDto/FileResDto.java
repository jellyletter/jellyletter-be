package com.be.jellyletter.dto.responseDto;

import lombok.Data;

@Data
public class FileResDto {

    private Integer id;
    private String fileName;
    private String contentType;
    private String fileUrl;
}
