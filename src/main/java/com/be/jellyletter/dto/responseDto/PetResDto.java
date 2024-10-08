package com.be.jellyletter.dto.responseDto;

import com.be.jellyletter.enums.Species;
import lombok.Data;

import java.util.List;

@Data
public class PetResDto {

    private Integer id;
    private String name;
    private Species species;
    private String ownerNickname;
    private String toyAndTreat;
    private String memory;
    private List<PetInfoResDto> petInfos;
}
