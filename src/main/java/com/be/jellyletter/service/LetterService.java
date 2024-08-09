package com.be.jellyletter.service;

import com.be.jellyletter.converter.LetterConverter;
import com.be.jellyletter.dto.requestDto.LetterReqDto;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetAiImageResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.enums.Species;
import com.be.jellyletter.model.Letter;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.PetAiImage;
import com.be.jellyletter.model.UserPet;
import com.be.jellyletter.repository.LetterRepository;
import com.be.jellyletter.repository.PetAiImageRepository;
import com.be.jellyletter.repository.PetRepository;
import com.be.jellyletter.repository.UserPetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LetterService {

    private final EntityManager entityManager;

    private final LetterRepository letterRepository;
    private final PetRepository petRepository;
    private final PetAiImageRepository petAiImageRepository;
    private final UserPetRepository userPetRepository;

    public LetterResDto createLetter(LetterReqDto letterReqDto) {
        Integer petId = letterReqDto.getPetResDto().getId();
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        Letter letter = letterReqDto.dtoToEntity(pet);

        // 반려동물이 보내는 메세지에는 AI 이미지 포함
        if (letterReqDto.getTypeCode() == 0) {
            PetAiImage randomPetAiImage = getRandomNoDupPetAiImage(pet.getId(), pet.getSpecies());
            letter.addPetAiImage(randomPetAiImage);
        }

        // 저장
        Letter savedLetter = letterRepository.save(letter);
        entityManager.refresh(savedLetter);

        // dto 로 변환
        LetterResDto letterResDto = LetterConverter.entityToDto(letter);

        // 반려동물이 보내는 메세지에는 호칭 변경
        if (letterResDto.getTypeCode() == 0) {
            PetAiImageResDto imageResDto = replaceOwnerNickname(letterResDto.getPetAiImage(), pet.getOwnerNickname());
            letterResDto.setPetAiImage(imageResDto);
        }

        return letterResDto;
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

    public List<LetterResDto> getAllUserPetLetters(Integer userId) {
        UserPet userPet = userPetRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserPet with userId: " + userId + " not found"));

        Integer petId = userPet.getPet().getId();
        String ownerNickname = userPet.getPet().getOwnerNickname();

        List<Letter> userPetLetters = letterRepository.findAllByPetIdOrderByCreatedAtDesc(petId);
        if (userPetLetters.isEmpty()) {
            throw new NoSuchElementException("Letter with PetId: " + petId + " not found");
        }

        List<LetterResDto> replacedUserPerLetters = new ArrayList<>();
        for (Letter userPetLetter : userPetLetters) {
            LetterResDto letterDto = LetterConverter.entityToDto(userPetLetter);
            PetAiImageResDto imageDto = letterDto.getPetAiImage();
            if (imageDto != null) {
                // 반려동물이 보낸 거면 메세지 내 호칭 변경해서 add
                PetAiImageResDto newImageDto = replaceOwnerNickname(imageDto, ownerNickname);
                letterDto.setPetAiImage(newImageDto);
                replacedUserPerLetters.add(letterDto);
            } else {
                // 사람이 보낸 거면 호칭 변경 없이 add
                replacedUserPerLetters.add(letterDto);
            }
        }

        return replacedUserPerLetters;
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

    private PetAiImageResDto replaceOwnerNickname(PetAiImageResDto petAiImageResDto, String ownerNickname) {
        String originalMessage = petAiImageResDto.getMessage();
        String newMessage = originalMessage.replace("{{Owner}}", ownerNickname);
        petAiImageResDto.setMessage(newMessage);

        return petAiImageResDto;
    }
}
