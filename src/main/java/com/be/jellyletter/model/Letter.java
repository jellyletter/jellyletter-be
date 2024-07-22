package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "letter")
@Getter
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Integer letterId;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet; // ManyToOne 관계

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "share_key", nullable = false, unique = true)
    private String shareKey;

    @Builder
    public Letter(Pet pet, String content, String shareKey) {
        this.pet = pet;
        this.content = content;
        this.shareKey = shareKey;
    }
}
