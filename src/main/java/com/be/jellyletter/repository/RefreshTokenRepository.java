package com.be.jellyletter.repository;

import com.be.jellyletter.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    boolean existsByKeyUserId(String keyUserId);
    void deleteByKeyUserId(String keyUserId);
}
