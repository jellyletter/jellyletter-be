package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "heart")
@Getter
@NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Integer heartId;

    @Column(name = "pet_id", nullable = false)
    private Integer petId;

    @Column(name = "letter_id", nullable = false)
    private Integer letterId;

    @Column(name = "user_phone", nullable = false, length = 13)
    private String userPhone;

    @Builder
    public Heart(Integer petId, Integer letterId, String userPhone) {
        this.petId = petId;
        this.letterId = letterId;
        this.userPhone = userPhone;
    }
}
