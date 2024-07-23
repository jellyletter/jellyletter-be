package com.be.jellyletter.service;

import com.be.jellyletter.converter.CounselingConverter;
import com.be.jellyletter.dto.responseDto.CounselingResDto;
import com.be.jellyletter.model.Counseling;
import com.be.jellyletter.repository.CounselingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CounselingService {

    private final CounselingRepository counselingRepository;

    public CounselingResDto getRandomCounseling() {
        Counseling counseling = counselingRepository.findRandomCounseling();

        return CounselingConverter.entityToDto(counseling);
    }
}
