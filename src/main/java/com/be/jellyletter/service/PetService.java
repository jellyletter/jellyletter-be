package com.be.jellyletter.service;

import com.be.jellyletter.converter.PetConverter;
import com.be.jellyletter.dto.PetDto;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.repository.PetRepository;
import com.be.jellyletter.repository.PetInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public PetDto createPet(PetDto petDto) {
        Pet pet = PetConverter.dtoToEntity(petDto);
        Pet savedPet = petRepository.save(pet);
        return PetConverter.entityToDto(savedPet);
    }
}
