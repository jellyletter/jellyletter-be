package com.be.jellyletter.service;

import com.be.jellyletter.converter.CounselingConverter;
import com.be.jellyletter.dto.responseDto.CounselingResDto;
import com.be.jellyletter.model.Counseling;
import com.be.jellyletter.model.Pet;
import com.be.jellyletter.repository.CounselingRepository;
import com.be.jellyletter.repository.PetRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CounselingService {

    private final CounselingRepository counselingRepository;
    private final PetRepository petRepository;

    public CounselingResDto getRandomCounselingWithPetName(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + petId + " not found"));

        Counseling counseling = counselingRepository.findRandomCounseling();
        CounselingResDto dto = CounselingConverter.entityToDto(counseling);

        String petName = pet.getName();

        // 이름 변경
        String rawContent = dto.getContent();
        String contentWithName = replacePetName(rawContent, petName);

        // 조사 매칭
        String contentWithFitJosa = replaceJosa(contentWithName, petName);

        dto.setContent(contentWithFitJosa);

        return dto;
    }

    private String replacePetName(String content, String petName) {
        return content.replace("{{반려동물}}", petName);
    }

    private String replaceJosa(String content, String petName) {
        char lastChar = petName.charAt(petName.length() -1);
        String newContent;

        if (hasFinalConsonant(lastChar)) {
            newContent = content.replace("은/는", "은");
            newContent = newContent.replace("이/와", "이와");
            newContent = newContent.replace("이/도", "이도");
            newContent = newContent.replace("아/야", "아");
        } else {
            newContent = content.replace("이/가", "가");
            newContent = newContent.replace("은/는", "는");
            newContent = newContent.replace("이/와", "와");
            newContent = newContent.replace("이/도", "도");
            newContent = newContent.replace("아/야", "야");
        }

        return newContent;
    }

    private static boolean hasFinalConsonant(char c) {
        return (c - 0xAC00) % 28 > 0;
    }

}
