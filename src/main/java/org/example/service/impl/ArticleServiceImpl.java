package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ArticleDTO;
import org.example.entity.Article;
import org.example.exception.NewsNotFoundException;
import org.example.repository.ArticleRepository;
import org.example.service.ArticleService;
import org.example.utils.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleMapper::convertToDto)
                .orElseThrow(() -> new NewsNotFoundException("Article not found!"));
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());

        return ArticleMapper.convertToDto(articleRepository.save(article));
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Article not found!"));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());


        return ArticleMapper.convertToDto(articleRepository.save(article));
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
