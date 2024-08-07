package com.be.jellyletter.repository;

import com.be.jellyletter.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Integer> {
    Optional<Letter> findByShareKey(String shareKey);
    @Query(value = "SELECT * FROM letter WHERE pet_id = :petId AND type_code = :typeCode ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<Letter> findTopByPetIdAAndTypeCodeOrderByCreatedDateDesc(@Param("petId") Integer petId, @Param("typeCode") Integer typeCode);
    List<Letter> findAllByPetId(Integer petId);
}
