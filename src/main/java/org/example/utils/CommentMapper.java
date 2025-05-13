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
        commentDTO.setUsername(comment.getUser () != null ? comment.getUser ().getUsername() : null); // Устанавливаем в null
        commentDTO.setPhotoUrl(null);
        commentDTO.setContent(comment.getContent());
        return commentDTO;
    }

    public static Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        // Не устанавливаем пользователя и статью здесь, они будут установлены в сервисе
        return comment;
    }
}
