package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@UtilityClass
public class UserMapper {

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhotoUrl(null);
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());

        return userDTO;
    }

    public static User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhotoUrl(userDTO.getPhotoUrl());

        return user;
    }
}