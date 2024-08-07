package com.be.jellyletter.service;

import com.be.jellyletter.converter.InfoConverter;
import com.be.jellyletter.dto.responseDto.InfoResDto;
import com.be.jellyletter.model.Info;
import com.be.jellyletter.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;

    public List<InfoResDto> getInfoListByGroupId(String groupId) {
        List<Info> infoList = infoRepository.findByIdGroupId(groupId);
        if (infoList.isEmpty()) {
            throw new NoSuchElementException("No Info found for groupId: " + groupId);
        }

        return infoList.stream()
                .map(InfoConverter::entityToDto)
                .collect(Collectors.toList());
    }
}
