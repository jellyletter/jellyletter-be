package com.be.jellyletter.repository;

import com.be.jellyletter.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Integer> {
}
