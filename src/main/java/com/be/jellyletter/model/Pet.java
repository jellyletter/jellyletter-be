package com.be.jellyletter.model;

import com.be.jellyletter.enums.Species;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="pet")
@Getter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species species;

    @Column(name = "owner_nickname", nullable = false)
    private String ownerNickname;

    @Column(name = "extra_desc")
    private String extraDesc;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetInfo> petInfos = new ArrayList<>();

    @Builder
    public Pet(String name, Species species, String ownerNickname, String extraDesc, String imageUrl) {
        this.name = name;
        this.species = species;
        this.ownerNickname = ownerNickname;
        this.extraDesc = extraDesc;
        this.imageUrl = imageUrl;
    }

    public void addPetInfos(List<PetInfo> petInfos) {
        this.petInfos.addAll(petInfos);
        petInfos.forEach(petInfo -> petInfo.setPet(this)); // Set bidirectional relationship
    }
}
