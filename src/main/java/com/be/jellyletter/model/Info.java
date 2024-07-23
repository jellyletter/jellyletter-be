package com.be.jellyletter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "info")
@Getter
@NoArgsConstructor
public class Info {

    @EmbeddedId
    private InfoId id;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(name = "code_name", length = 100, nullable = false)
    private String codeName;

    @Column(name = "orders", nullable = false)
    private Integer orders;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoId implements Serializable {
        @Column(name = "group_id")
        private String groupId;

        @Column(name = "code")
        private Integer code;
    }

    @Builder
    public Info(InfoId id, String groupName, String codeName, Integer orders, String useYn) {
        this.id = id;
        this.groupName = groupName;
        this.codeName = codeName;
        this.orders = orders;
        this.useYn = useYn;
    }
}
