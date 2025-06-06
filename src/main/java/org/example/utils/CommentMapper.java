package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.CommentDTO;
import org.example.entity.Comment;

@UtilityClass
public class CommentMapper {

    public static CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setArticleId(comment.getArticle() != null ? comment.getArticle().getId() : null);
        commentDTO.setUser_id(comment.getUser () != null ? comment.getUser ().getId() : null);
        commentDTO.setUsername(comment.getUser () != null ? comment.getUser ().getUsername() : null);
        commentDTO.setPhotoUrl(comment.getUser().getPhotoUrl());
        commentDTO.setContent(comment.getContent());
        return commentDTO;
    }

    public static Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        return comment;
    }
}
