package com.be.jellyletter.repository;

import com.be.jellyletter.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Integer> {
    Optional<Letter> findByShareKey(String shareKey);
    List<Letter> findAllByPetId(Integer petId);
}
