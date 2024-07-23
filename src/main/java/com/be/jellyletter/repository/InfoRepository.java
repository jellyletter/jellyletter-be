package com.be.jellyletter.repository;

import com.be.jellyletter.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoRepository extends JpaRepository<Info, Info.InfoId> {
    List<Info> findByIdGroupId(String groupId);
}
