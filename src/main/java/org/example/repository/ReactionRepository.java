package org.example.repository;

import org.example.entity.Article;
import org.example.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByArticleId(Long articleId);
    void deleteByArticle(Article article);
    List<Reaction> findByUserIdAndArticleId(Long userId, Long articleId);


}
