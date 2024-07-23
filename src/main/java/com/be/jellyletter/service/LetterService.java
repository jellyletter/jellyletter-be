package com.be.jellyletter.service;

import com.be.jellyletter.converter.LetterConverter;
import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.repository.LetterRepository;
import com.be.jellyletter.repository.PetRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterService {

    private final EntityManager entityManager;

    private final LetterRepository letterRepository;
    private final PetRepository petRepository;

    public LetterResDto createLetter(LetterReqDto letterReqDto) {
        Integer petId = letterReqDto.getPetResDto().getId();
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        Letter letter = letterReqDto.dtoToEntity(pet);
        Letter savedLetter = letterRepository.save(letter);
        entityManager.refresh(savedLetter);

        return LetterConverter.entityToDto(savedLetter);
    }
}
