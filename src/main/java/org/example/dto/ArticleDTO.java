package org.example.dto;

import lombok.Data;
import org.example.entity.Comment;
import org.example.entity.Reaction;

import java.util.List;

@Data
public class ArticleDTO {
    private long id;
    private String title;
    private String content;
    private List<Comment> comments;
    private List<Reaction> reactions;
    private String username;
    private String photoUrl;


}
