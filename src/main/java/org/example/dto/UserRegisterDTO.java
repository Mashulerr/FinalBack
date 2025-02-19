package org.example.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {


    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String photoUrl;

}