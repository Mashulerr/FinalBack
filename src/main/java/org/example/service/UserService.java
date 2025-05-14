package org.example.service;

import org.example.dto.UserDTO;
import org.example.dto.UserRegisterDTO;
import org.example.entity.User;

import java.util.List;

public interface UserService {


    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO createUser(UserRegisterDTO dto);

    UserDTO updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);

    UserDTO getUserByUsername(String username);

    User getUserEntityById(Long id);
}