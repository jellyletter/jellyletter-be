package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.User;
import com.be.jellyletter.model.UserPet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPetReqDto {

    @NotBlank(message = "UserId cannot be blank")
    @Schema(description = "유저 ID", example = "100")
    private Integer userId;

    @NotBlank(message = "PetId cannot be blank")
    @Schema(description = "펫 ID", example = "200")
    private Integer petId;

    public UserPet dtoToEntity(User user, Pet pet) {
        UserPet.UserPetId userPetId = new UserPet.UserPetId(this.userId, this.petId);

        return UserPet.builder()
                .id(userPetId)
                .user(user)
                .pet(pet)
                .build();
    }
}
