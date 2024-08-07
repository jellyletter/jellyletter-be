package com.be.jellyletter.service;

import com.be.jellyletter.converter.PetConverter;
import com.be.jellyletter.dto.requestDto.PetReqDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.repository.PetRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final EntityManager entityManager;

    private final PetRepository petRepository;

    public PetResDto createPet(PetReqDto petReqDto) {
        Pet pet = petReqDto.dtoToEntity();
        Pet savedPet = petRepository.save(pet);
        entityManager.refresh(savedPet);

        return PetConverter.entityToDto(savedPet);
    }

    public PetResDto getPetById(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        return PetConverter.entityToDto(pet);
    }
}
