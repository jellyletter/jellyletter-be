package com.be.jellyletter.dto.responseDto;

import com.be.jellyletter.enums.Species;
import lombok.Data;

@Data
public class PetAiImageResDto {

    private Integer id;
    private Species species;
    private String imageUrl;
    private String message;
}
