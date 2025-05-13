package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.ArticleDTO;
import org.example.dto.CommentDTO;
import org.example.entity.Article;
import org.example.entity.User;


import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ArticleMapper {

    public static Article convertToEntity(ArticleDTO articleDTO, User user) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUser (user); // Устанавливаем пользователя
        return article;
    }

    public static ArticleDTO convertToDto(Article article, User user) {
        ArticleDTO dto = new ArticleDTO();

        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setUsername(user != null ? user.getUsername() : null); // Устанавливаем в null
        dto.setPhotoUrl(null);
        dto.setLikes(article.getLikes());
        dto.setDislikes(article.getDislikes());

        List<CommentDTO> commentDTOs = article.getComments().stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setId(comment.getId());
                    commentDTO.setContent(comment.getContent());

                    User commentUser  = comment.getUser ();
                    commentDTO.setUsername(commentUser  != null ? commentUser .getUsername() : null); // Устанавливаем в null
                    commentDTO.setPhotoUrl(null);
                    commentDTO.setUser_id(commentUser  != null ? commentUser .getId() : null); // Устанавливаем в null
                    commentDTO.setArticleId(article.getId()); // ID статьи

                    return commentDTO;
                })
                .collect(Collectors.toList());

        dto.setComments(commentDTOs);
        return dto;
    }
}
