package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "letter")
@Getter
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Integer letterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "type_code", nullable = false)
    private Integer typeCode;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private PetAiImage petAiImage;

    @Column(name = "share_key", nullable = false, unique = true)
    private String shareKey;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Builder
    public Letter(Pet pet, Integer typeCode, String content) {
        this.pet = pet;
        this.typeCode = typeCode;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    public void addPetAiImage(PetAiImage petAiImage) {
        this.petAiImage = petAiImage;
    }

    public void updatePetAiImage(PetAiImage newPetAiImage) {
        this.petAiImage= newPetAiImage;
    }
}
