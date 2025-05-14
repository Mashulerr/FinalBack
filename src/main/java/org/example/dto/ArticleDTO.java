package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String photoUrl;
    private int likes;
    private int dislikes;
    private List<CommentDTO> comments;
    private boolean isFavorite;
}
