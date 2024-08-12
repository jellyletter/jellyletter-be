package com.be.jellyletter.dto.requestDto;

import com.be.jellyletter.model.Pet;
import com.be.jellyletter.model.User;
import com.be.jellyletter.model.UserPet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPetReqDto {

    @NotNull(message = "UserId cannot be blank")
    @Schema(description = "유저 ID", example = "100")
    private Integer userId;

    @NotNull(message = "PetId cannot be blank")
    @Schema(description = "펫 ID", example = "200")
    private Integer petId;

    public UserPet dtoToEntity(User user, Pet pet) {
        return UserPet.builder()
                .user(user)
                .pet(pet)
                .build();
    }
}
