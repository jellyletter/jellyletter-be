package com.be.jellyletter.repository;

import com.be.jellyletter.model.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPetRepository extends JpaRepository<UserPet, UserPet.UserPetId> {
}
