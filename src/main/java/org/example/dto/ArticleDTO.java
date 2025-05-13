package org.example.dto;

import lombok.Data;
import org.example.entity.Comment;
import org.example.entity.Reaction;

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
}
