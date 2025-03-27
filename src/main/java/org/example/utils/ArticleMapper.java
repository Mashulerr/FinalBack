package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.ArticleDTO;
import org.example.dto.CommentDTO;
import org.example.dto.UserDTO;
import org.example.entity.Article;
import org.example.entity.Comment;
import org.example.entity.User;
import org.example.service.UserService;

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

        dto.setUsername(user.getUsername());
        dto.setPhotoUrl(user.getPhotoUrl());
        return dto;
    }
}