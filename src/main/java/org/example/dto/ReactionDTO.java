package org.example.dto;


import lombok.Data;

@Data
public class ReactionDTO {
    private Long id;
    private Long articleId;
    private String type;
    private Long userId;
}