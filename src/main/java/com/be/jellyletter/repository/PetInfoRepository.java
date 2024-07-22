package com.be.jellyletter.repository;

import com.be.jellyletter.model.PetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo, Integer> {

}
