package com.be.jellyletter.service;

import com.be.jellyletter.converter.HeartConverter;
import com.be.jellyletter.dto.requestDto.HeartReqDto;
import com.be.jellyletter.dto.responseDto.HeartResDto;
import com.be.jellyletter.model.Heart;
import com.be.jellyletter.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    public HeartResDto createHeart(HeartReqDto heartReqDto) {
        Heart heart = heartReqDto.dtoToEntity();
        Heart savedHeart = heartRepository.save(heart);

        return HeartConverter.entityToDto(savedHeart);
    }
}
