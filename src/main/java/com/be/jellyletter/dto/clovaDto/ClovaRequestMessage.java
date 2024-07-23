package com.be.jellyletter.dto.clovaDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClovaRequestMessage {

    private String role;
    private String content;
}
