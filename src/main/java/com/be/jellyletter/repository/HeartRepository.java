package com.be.jellyletter.repository;

import com.be.jellyletter.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Integer> {
}
