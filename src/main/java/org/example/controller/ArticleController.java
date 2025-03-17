package org.example.controller;

import org.example.dto.ArticleDTO;
import org.example.entity.Article;
import org.example.exception.InvalidNewsException;
import org.example.exception.NewsNotFoundException;
import org.example.repository.ArticleRepository;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class ArticleController {
        @Autowired
        private ArticleRepository articleRepository;
        private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDTO> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        if (article.getTitle() == null || article.getTitle().isEmpty()) {
            throw new InvalidNewsException("Название новости не может быть пустым");
        }
        return ResponseEntity.ok(articleRepository.save(article));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NewsNotFoundException("Новость с ID " + id + " не найдена"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        ArticleDTO updatedArticle = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
    }

