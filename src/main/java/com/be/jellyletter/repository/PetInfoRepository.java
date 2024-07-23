package com.be.jellyletter.repository;

import com.be.jellyletter.model.PetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo, Integer> {

    @Query("SELECT pi FROM PetInfo pi JOIN FETCH pi.info WHERE pi.infoId = :infoId")
    Optional<PetInfo> findByIdWithInfo(@Param("infoId") Integer infoId);
}
