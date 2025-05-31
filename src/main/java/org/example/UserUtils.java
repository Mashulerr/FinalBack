package org.example;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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

            UserDTO user = (UserDTO) authentication.getPrincipal();
            return user.getId();
        }
        throw new RuntimeException("No authenticated user found!");
    }
}