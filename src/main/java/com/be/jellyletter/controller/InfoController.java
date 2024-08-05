package com.be.jellyletter.controller;

import com.be.jellyletter.dto.responseDto.InfoResDto;
import com.be.jellyletter.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/info")
@Validated
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @GetMapping
    @Operation(summary = "선택형 문항 선택지(성격 유형) 조회 API", description = "특정 그룹 ID 에 해당하는 선택형 문항 선택지를 조회합니다. 현재는 성격(G0001)만 있습니다.")
    public ResponseEntity<List<InfoResDto>> getInfoListByGroupId(@RequestParam(name = "groupId") String groupId) {
        List<InfoResDto> responseDtoList = infoService.getInfoListByGroupId(groupId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
}
