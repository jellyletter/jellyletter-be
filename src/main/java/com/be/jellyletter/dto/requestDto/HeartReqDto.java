package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.Heart;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HeartReqDto {

    private String userPhone;

    public Heart dtoToEntity() {
        return Heart.builder()
                .userPhone(this.userPhone)
                .build();
    }
}
