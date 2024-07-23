package com.be.jellyletter.repository;

import com.be.jellyletter.model.Counseling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CounselingRepository extends JpaRepository<Counseling, Integer> {

    @Query(value = "SELECT * FROM counseling ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Counseling findRandomCounseling();
}
