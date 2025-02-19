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
import org.springframework.security.access.prepost.PreAuthorize;

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
    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id){

        return userRepository.findById(id)
                .map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException("User not found!")) ;
    }

    @Override
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
    public UserDTO updateUser(Long id, UserDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        if(userRepository.findByUsername(dto.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username already exists!");

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setPhone(dto.getPhone());

        return UserMapper.convertToDto(userRepository.save(user));

    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(Long id){
        System.out.println("Delete: " + id);

        userRepository.deleteUserById(id);
    }


}