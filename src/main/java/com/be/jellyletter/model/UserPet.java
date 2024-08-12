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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userPetId")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "user_pet_ibfk_1"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "user_pet_ibfk_2"))
    private Pet pet;

    @Builder
    public UserPet(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
    }

    public void updatePet(Pet pet) {
        this.pet = pet;
    }

}
