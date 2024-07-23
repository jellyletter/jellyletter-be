package com.be.jellyletter.dto.requestDto;
import com.be.jellyletter.model.Heart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HeartReqDto {

    @Schema(description = "사용자 전화번호(옵션)", example = "01012345678")
    private String userPhone;

    public Heart dtoToEntity() {
        return Heart.builder()
                .userPhone(this.userPhone)
                .build();
    }
}
