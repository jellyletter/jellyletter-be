package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="user_pet")
@Getter
@NoArgsConstructor
public class UserPet {

    @EmbeddedId
    private UserPetId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "user_pet_ibfk_1"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "user_pet_ibfk_2"))
    private Pet pet;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPetId implements Serializable {
        @Column(name = "user_id", nullable = false)
        private Integer userId;

        @Column(name = "pet_id", nullable = false)
        private Integer petId;
    }

    @Builder
    public UserPet(UserPetId id, User user, Pet pet) {
        this.id = id;
        this.user = user;
        this.pet = pet;
    }

}
