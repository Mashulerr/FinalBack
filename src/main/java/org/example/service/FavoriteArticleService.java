package org.example.service;

import javassist.NotFoundException;
import org.example.UserUtils;
import org.example.dto.ArticleDTO;


import java.util.List;

public interface FavoriteArticleService {
    void addToFavorites(Long userId, Long articleId);
    void removeFromFavorites(Long userId, Long articleId);
    List<ArticleDTO> getUserFavorites(Long userId);
    boolean isArticleInFavorites(Long userId, Long articleId);
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
}