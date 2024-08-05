package com.be.jellyletter.model;

import com.be.jellyletter.enums.Oauth2Vendor;
import com.be.jellyletter.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role userRole;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor", nullable = false)
    private Oauth2Vendor vendor;

    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @Builder
    public User(
            String username, Role userRole,
            String nickname, String email, String userPhone, Oauth2Vendor vendor, Integer userStatus) {
        this.username = username;
        this.userRole = userRole;
        this.nickname = nickname;
        this.email = email;
        this.userPhone = userPhone;
        this.vendor = vendor;
        this.userStatus = userStatus;
    }
}
