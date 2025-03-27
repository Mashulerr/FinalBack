package org.example.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long articleId;
    private String content;
    private Long user_id;
}
