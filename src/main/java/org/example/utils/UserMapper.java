package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.UserDTO;
import org.example.entity.User;

@UtilityClass
public class UserMapper {

    public UserDTO convertToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhotoUrl(user.getPhotoUrl());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());

        return userDTO;
    }
}