package com.be.jellyletter.service;

import com.be.jellyletter.converter.LetterConverter;
import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetAiImage;
import com.be.jellyletter.repository.LetterRepository;
import com.be.jellyletter.repository.PetAiImageRepository;
import com.be.jellyletter.repository.PetRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterService {

    private final EntityManager entityManager;

    private final LetterRepository letterRepository;
    private final PetRepository petRepository;
    private final PetAiImageRepository petAiImageRepository;

    public LetterResDto createLetter(LetterReqDto letterReqDto) {
        Integer petId = letterReqDto.getPetResDto().getId();
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        Letter letter = letterReqDto.dtoToEntity(pet);

        // 반려동물이 보내는 메세지에는 AI 이미지 포함
        if (letterReqDto.getTypeCode() == 0) {
            PetAiImage randomPetAiImage = getRandomNoDupPetAiImage(pet.getId(), pet.getSpecies());
            PetAiImage petAiImage = replaceOwnerNickname(randomPetAiImage, pet.getOwnerNickname());
            letter.addPetAiImage(petAiImage);
        }

        Letter savedLetter = letterRepository.save(letter);
        entityManager.refresh(savedLetter);

        return LetterConverter.entityToDto(savedLetter);
    }

    public LetterResDto getLetterByShareKey(String shareKey) {
        Letter letter = letterRepository.findByShareKey(shareKey)
                .orElseThrow(() -> new NoSuchElementException("Letter with ShareKey " + shareKey + " not found"));

        return LetterConverter.entityToDto(letter);
    }

    public LetterResDto getLastLetterByPetIdAndTypeCode(Integer petId, Integer typeCode) {
        Letter letter = letterRepository.findTopByPetIdAAndTypeCodeOrderByCreatedDateDesc(petId, typeCode)
                .orElseThrow(() -> new NoSuchElementException("Letter with PetId: " + petId + " TypeCode: " + typeCode + " not found"));

        return LetterConverter.entityToDto(letter);
    }

    public List<LetterResDto> getAllUserPetLetters(Integer petId) {
        List<Letter> userPetLetters = letterRepository.findAllByPetId(petId);
        if (userPetLetters.isEmpty()) {
            throw new NoSuchElementException("Letter with PetId: " + petId + " not found");
        }

        return userPetLetters.stream()
                .map(LetterConverter::entityToDto)
                .toList();
    }

    private PetAiImage getRandomNoDupPetAiImage(Integer petId, Species species) {
        // 해당 반려동물이 보낸 편지 리스트 조회
        List<Letter> petLetters = letterRepository.findAllByPetId(petId);

        // Pet Ai 이미지 전체 조회
        List<PetAiImage> allPetAiImageForSpecies = petAiImageRepository.findAllBySpecies(species);

        // 전체 이미지 ID 추출
        Set<Integer> allPetAiImageIds = allPetAiImageForSpecies.stream()
                .map(PetAiImage::getId)
                .collect(Collectors.toSet());

        // 랜덤으로 고를 이미지 집합 연산
        Set<Integer> notSendPetAiImageIds;
        if (!petLetters.isEmpty()) {
            // 보낸 이미지 ID 추출
            Set<Integer> letterPetAiImageIds = petLetters.stream()
                    .map(Letter::getPetAiImage)
                    .filter(Objects::nonNull)
                    .map(PetAiImage::getId)
                    .collect(Collectors.toSet());

            // 차집합
            notSendPetAiImageIds = allPetAiImageIds.stream()
                    .filter(id -> !letterPetAiImageIds.contains(id))
                    .collect(Collectors.toSet());

        } else {
            notSendPetAiImageIds = allPetAiImageIds;
        }


        // 차집합 중 랜덤 선택
        if (notSendPetAiImageIds.isEmpty()) {
            throw new NoSuchElementException("No more Pet ai image for petId: " + petId);
        }

        int randomIndex = new Random().nextInt(notSendPetAiImageIds.size());
        Integer randomId = notSendPetAiImageIds.stream().skip(randomIndex).findFirst().orElseThrow();

        return petAiImageRepository.findById(randomId)
                .orElseThrow(() -> new NoSuchElementException("PetAiImage not found with id: " + randomId));
    }

    private PetAiImage replaceOwnerNickname(PetAiImage petAiImage, String ownerNickname) {
        String originalMessage = petAiImage.getMessage();
        String newMessage = originalMessage.replace("{{주인을 부르는 방식}}", ownerNickname);
        petAiImage.updateMessage(newMessage);

        return petAiImage;
    }
}
