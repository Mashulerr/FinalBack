package org.example.utils;

import org.example.dto.FavoriteArticleDTO;
import org.example.entity.Article;
import org.example.entity.FavoriteArticle;
import org.example.entity.User;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;

@Component
public class FavoriteArticlesMapper {

    public FavoriteArticleDTO toDto(FavoriteArticle favoriteArticle) {
        if (favoriteArticle == null) {
            return null;
        }

        Long userId = null;
        Long articleId = null;

        if (favoriteArticle.getUser() != null) {
            userId = favoriteArticle.getUser().getId();
        }

        if (favoriteArticle.getArticle() != null) {
            articleId = favoriteArticle.getArticle().getId();
        }

        return FavoriteArticleDTO.builder()
                .id(favoriteArticle.getId())
                .userId(userId)
                .articleId(articleId)
                .build();
    }

    public FavoriteArticle toEntity(FavoriteArticleDTO dto) {
        if (dto == null) {
            return null;
        }

        FavoriteArticle favoriteArticle = new FavoriteArticle();
        favoriteArticle.setId(dto.getId());
        return favoriteArticle;
    }
}