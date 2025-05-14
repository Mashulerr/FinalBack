package org.example;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class UserUtils {
    public static Long getCurrentUser_id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Предполагается, что ваш пользовательский объект содержит метод getId()
            UserDTO user = (UserDTO) authentication.getPrincipal();
            return user.getId(); // Возвращаем ID текущего пользователя
        }
        throw new RuntimeException("No authenticated user found!");
    }
}