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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
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

        // 유저 ID로 저장된 펫 내역이 있는지 확인(현재는 1:1 매칭만 지원하기 때문)
        Optional<UserPet> existUserPet = userPetRepository.findByUserId(userId);

        UserPet savedUserPet;

        // 저장된 내역이 없으면 새로 저장
        if (existUserPet.isEmpty()) {
            UserPet userPet = userPetReqDto.dtoToEntity(user, pet);
            savedUserPet = userPetRepository.save(userPet);
        }
        // 저장된 내역이 있으면 수정하여 저장
        else {
            UserPet userPet = existUserPet.get();
            userPet.updatePet(pet);
            System.out.println("Updated UserPet entity with new Pet: " + userPet.getPet().getId());
            savedUserPet = userPetRepository.save(userPet);
        }
        userPetRepository.flush();

        return UserPetConverter.entityToDto(savedUserPet);
    }

    public UserPetResDto getUserPetById(Integer userId, Integer petId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        UserPet userPet = userPetRepository.findByUserAndPet(user, pet)
                .orElseThrow(() -> new NoSuchElementException("User-Pet with userId: " + userId + " and petId: " + petId + " not found"));

        return UserPetConverter.entityToDto(userPet);
    }
}
