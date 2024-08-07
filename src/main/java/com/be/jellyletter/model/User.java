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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_phone", nullable = true)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor", nullable = false)
    private Oauth2Vendor vendor;

    // 0 회원, 1 탈퇴, 2 휴면
    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @Builder
    public User(
            String username, Role userRole,String email, Oauth2Vendor vendor, Integer userStatus) {
        this.username = username;
        this.email = email;

        this.vendor = vendor;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public void addUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

}
