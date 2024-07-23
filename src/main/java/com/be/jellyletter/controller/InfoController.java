package com.be.jellyletter.controller;

import com.be.jellyletter.dto.responseDto.InfoResDto;
import com.be.jellyletter.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@Validated
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @GetMapping
    public ResponseEntity<List<InfoResDto>> getInfoListByGroupId(@RequestParam(name = "groupId") String groupId) {
        List<InfoResDto> responseDtoList = infoService.getInfoListByGroupId(groupId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
}
