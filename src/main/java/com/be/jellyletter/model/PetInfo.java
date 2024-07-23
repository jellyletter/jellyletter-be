package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet_info")
@Getter
@NoArgsConstructor
public class PetInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Integer infoId; // 단일 auto_increment 기본 키

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet; // ManyToOne 관계

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "code", nullable = false)
    private Integer code;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = false, updatable = false),
            @JoinColumn(name = "code", referencedColumnName = "code", insertable = false, updatable = false)
    })
    private Info info;

    @Builder
    public PetInfo(Pet pet, String groupId, Integer code) {
        this.pet = pet;
        this.groupId = groupId;
        this.code = code;
    }

    @Builder
    public PetInfo(String groupId, Integer code) {
        this.groupId = groupId;
        this.code = code;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getCodeName() {
        return this.info != null ? this.info.getCodeName() : null;
    }
}
