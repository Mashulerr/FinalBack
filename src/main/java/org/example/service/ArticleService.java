package org.example.service;

import org.example.dto.ArticleDTO;
import org.example.entity.Article;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getAllArticles();

    ArticleDTO getArticleById(Long id);

    ArticleDTO createArticle(ArticleDTO dto);

    ArticleDTO updateArticle(Long id, ArticleDTO dto);
    Article getArticleEntityById(Long id);

    void deleteArticle(Long id);
}