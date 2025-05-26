package org.example.utils;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.example.UserUtils;
import org.example.dto.ArticleDTO;
import org.example.dto.CommentDTO;
import org.example.dto.UserDTO;
import org.example.dto.UserRegisterDTO;
import org.example.entity.Article;
import org.example.entity.User;
import org.example.service.FavoriteArticleService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class ArticleMapper {

    private static FavoriteArticleService favoriteArticleService;
    public static void setFavoriteArticleService(FavoriteArticleService service) {
        favoriteArticleService = service;
    }

    public static Article convertToEntity(ArticleDTO articleDTO, User user) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUser(user);
        return article;
    }

    public static ArticleDTO convertToDto(Article article, User user) {
        ArticleDTO dto = new ArticleDTO();

        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setUsername(user != null ? user.getUsername() : null);
        dto.setPhotoUrl(user != null ? user.getPhotoUrl() : null);
        dto.setLikes(article.getLikes());
        dto.setDislikes(article.getDislikes());


        List<CommentDTO> commentDTOs = article.getComments() != null ?
                article.getComments().stream()
                        .map(comment -> {
                            CommentDTO commentDTO = new CommentDTO();
                            commentDTO.setId(comment.getId());
                            commentDTO.setContent(comment.getContent());

                            User commentUser = comment.getUser();
                            commentDTO.setUsername(commentUser != null ? commentUser.getUsername() : null);
                            commentDTO.setPhotoUrl(commentUser != null ? commentUser.getPhotoUrl() : null);
                            commentDTO.setUser_id(commentUser != null ? commentUser.getId() : null);
                            commentDTO.setArticleId(article.getId());

                            return commentDTO;
                        })
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        dto.setComments(commentDTOs);


        Long currentUserId = UserUtils.getCurrentUser_id();
        if (currentUserId != null && favoriteArticleService != null) {
            dto.setFavorite(favoriteArticleService.isArticleInFavorites(currentUserId, article.getId()));
        } else {
            dto.setFavorite(false);
        }

        return dto;
    }
}