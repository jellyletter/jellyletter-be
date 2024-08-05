package com.be.jellyletter.service;

import com.be.jellyletter.converter.UserPetConverter;
import com.be.jellyletter.dto.requestDto.UserPetReqDto;
import com.be.jellyletter.dto.responseDto.UserPetResDto;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.User;
import com.be.jellyletter.model.UserPet;
import com.be.jellyletter.repository.PetRepository;
import com.be.jellyletter.repository.UserPetRepository;
import com.be.jellyletter.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPetService {

    private final UserPetRepository userPetRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public UserPetResDto createUserPet(UserPetReqDto userPetReqDto) {
        Integer userId = userPetReqDto.getUserId();
        Integer petId = userPetReqDto.getPetId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        UserPet userPet = userPetReqDto.dtoToEntity(user, pet);
        UserPet savedUserPet = userPetRepository.save(userPet);

        return UserPetConverter.entityToDto(savedUserPet);
    }

    public UserPetResDto getUserPetById(Integer userId, Integer petId) {
        UserPet.UserPetId userPetId = new UserPet.UserPetId(userId, petId);
        UserPet userPet = userPetRepository.findById(userPetId)
                .orElseThrow(() -> new NoSuchElementException("User-Pet with userId: " + userId + " and petId: " + petId + " not found"));

        return UserPetConverter.entityToDto(userPet);
    }
}
