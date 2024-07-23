package com.be.jellyletter.repository;

import com.be.jellyletter.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info, Info.InfoId> {
}
