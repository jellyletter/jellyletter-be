package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String refreshToken;

    private String keyUserId;

    @Builder
    public RefreshToken(String keyUserId, String refreshToken) {
        this.keyUserId = keyUserId;
        this.refreshToken = refreshToken;
    }
}
