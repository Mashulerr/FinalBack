package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.CommentDTO;
import org.example.entity.Comment;

@UtilityClass
public class CommentMapper {

    public CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setArticleId(comment.getArticle() != null ? comment.getArticle().getId() : null);
        commentDTO.setContent(comment.getContent());
        commentDTO.setUser_id(comment.getUser() != null ? comment.getUser().getId() : null);



        return commentDTO;
    }

    public Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        commentDTO.setUser_id(comment.getUser() != null ? comment.getUser().getId() : null);



        return comment;
    }
}