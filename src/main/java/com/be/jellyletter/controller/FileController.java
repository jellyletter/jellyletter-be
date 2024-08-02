package com.be.jellyletter.controller;

import com.be.jellyletter.dto.responseDto.FileResDto;
import com.be.jellyletter.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@Validated
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "파일 업로드 API",
            description = "파일을 업로드하면 저장 후 url을 반환합니다."
    )
    public ResponseEntity<FileResDto> uploadFile(
            @Parameter(
                    description = "multipart/form-data 형식의 이미지를 input으로 받습니다. 이때 key 값은 file 입니다.",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "file")MultipartFile file) throws IOException {
        String fileUrl = fileService.uploadFile(file, "asset");
        FileResDto responseDto = fileService.saveFile(file, fileUrl);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
