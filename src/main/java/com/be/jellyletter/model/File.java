package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="file")
@Getter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Builder
    public File(String fileName, String contentType, String fileUrl) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileUrl = fileUrl;
    }


}
