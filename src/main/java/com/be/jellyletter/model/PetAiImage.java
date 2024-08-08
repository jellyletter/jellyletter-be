package com.be.jellyletter.model;

import com.be.jellyletter.enums.Species;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pet_ai_image")
@Getter
@NoArgsConstructor
public class PetAiImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", insertable = false, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false, insertable = false, updatable = false)
    private Species species;

    @Column(name = "image_url", nullable = false, insertable = false, updatable = false)
    private String imageUrl;

    @Column(name = "message", nullable = false, insertable = false, updatable = false)
    private String message;

    public void updateMessage(String newMessage) {
        this.message = newMessage;
    }
}
