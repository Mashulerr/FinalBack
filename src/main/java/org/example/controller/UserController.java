package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.example.dto.UserDTO;
import org.example.dto.UserRegisterDTO;

import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class UserController {

    private final UserService personService;

    @GetMapping
    public List<UserDTO> getAllPersons() {
        return personService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createPerson(@RequestBody UserRegisterDTO dto) {
        UserDTO createdUser  = personService.createUser (dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser );

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updatePerson(@PathVariable Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(personService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<String> getByUsername(@PathVariable String username) {
        UserDTO userDTO = personService.getUserByUsername(username);
        return ResponseEntity.ok("User " + userDTO.getUsername() + " is registered");
    }

    @GetMapping("/login")
    public ResponseEntity<UserDTO> login(Authentication authentication) {
        return ResponseEntity.ok(personService.getUserByUsername(authentication.getName()));
    }


}