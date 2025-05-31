package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.utils.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.dto.UserRegisterDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) throw new UserNotFoundException("User with username " + username + " not found");
        return UserMapper.convertToDto(userOptional.get());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {

        return userRepository.findById(id)
                .map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public UserDTO createUser(UserRegisterDTO dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username already exists");

        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return UserMapper.convertToDto(userRepository.save(user));
    }

    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User  not found!"));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));


        if (dto.getUsername() != null && !dto.getUsername().isBlank()
                && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("Username already exists!");
            }
            user.setUsername(dto.getUsername());
        }


        if (dto.getName() != null && !dto.getName().isBlank() && !dto.getName().equals("string")) {
            user.setName(dto.getName());
        }


        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !dto.getEmail().equals("string")) {
            user.setEmail(dto.getEmail());
        }


        if (dto.getPhotoUrl() != null) {
            user.setPhotoUrl(dto.getPhotoUrl());
        }


        if (dto.getPhone() != null && !dto.getPhone().isBlank() && !dto.getPhone().equals("string")) {
            user.setPhone(dto.getPhone());
        }

        return UserMapper.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        userRepository.delete(user);
    }
}
