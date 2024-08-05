package com.be.jellyletter.repository;

import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.PetAiImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetAiImageRepository extends JpaRepository<PetAiImage, Integer> {
    List<PetAiImage> findAllBySpecies(Species species);
}
