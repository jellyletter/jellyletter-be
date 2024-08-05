package com.be.jellyletter.dto.responseDto;

import com.be.jellyletter.model.User;
import lombok.Data;

@Data
public class UserPetResDto {

    private User user;
    private PetResDto petResDto;
}
