package com.be.jellyletter.auth.dto;

import com.be.jellyletter.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role을 권한으로 변환해서 반환합니다.
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        // Password는 User 엔티티에서 직접 제공되지 않으므로 null로 반환합니다.
        // 필요시 엔티티에 password 필드를 추가해야 합니다.
        return null;
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // email을 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        // 이 예제에서는 모든 계정이 만료되지 않은 것으로 가정합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 이 예제에서는 모든 계정이 잠기지 않은 것으로 가정합니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 이 예제에서는 모든 자격 증명이 만료되지 않은 것으로 가정합니다.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 이 예제에서는 모든 계정이 활성화된 것으로 가정합니다.
        // userStatus를 사용하여 비활성 상태를 처리할 수 있습니다.
        // 0: 활성, 1: 탈퇴, 2: 휴면
        return user.getUserStatus() != null && user.getUserStatus() == 0;
    }
}
