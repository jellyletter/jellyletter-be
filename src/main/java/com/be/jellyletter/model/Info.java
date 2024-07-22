package com.be.jellyletter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "info")
@Getter
@NoArgsConstructor
public class Info {

    @Id
    @Column(name = "group_id", length = 5)
    private String groupId;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Id
    @Column(name = "code")
    private Integer code;

    @Column(name = "code_name", length = 100, nullable = false)
    private String codeName;

    @Column(name = "orders", nullable = false)
    private Integer orders;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

}
