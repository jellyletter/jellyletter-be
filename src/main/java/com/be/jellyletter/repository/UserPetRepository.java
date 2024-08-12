package com.be.jellyletter.repository;

import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.User;
import com.be.jellyletter.model.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPetRepository extends JpaRepository<UserPet, Integer> {

    Optional<UserPet> findByUserId(Integer userId);
    Optional<UserPet> findByUserAndPet(User user, Pet pet);
}
