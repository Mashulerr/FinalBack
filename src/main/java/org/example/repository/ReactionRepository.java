package org.example.repository;

import org.example.entity.Article;
import org.example.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByArticleId(Long articleId);
    void deleteByArticle(Article article);
}
