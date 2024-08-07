package com.be.jellyletter.repository;

import com.be.jellyletter.model.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPetRepository extends JpaRepository<UserPet, UserPet.UserPetId> {

    Optional<UserPet> findByUserId(Integer userId);
}
