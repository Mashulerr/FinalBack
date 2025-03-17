package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.ArticleDTO;
import org.example.entity.Article;

@UtilityClass
public class ArticleMapper {

    public ArticleDTO convertToDto(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());


        return articleDTO;
    }
}