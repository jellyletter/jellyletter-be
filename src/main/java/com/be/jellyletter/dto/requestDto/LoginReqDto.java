package com.be.jellyletter.dto.requestDto;

import lombok.Data;

@Data
public class LoginReqDto {

    private String provider;
    private String name;
    private String nickname;
    private String email;
    private String mobile;
}
