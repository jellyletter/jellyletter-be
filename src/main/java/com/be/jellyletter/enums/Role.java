package com.be.jellyletter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "운영자"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;
}
