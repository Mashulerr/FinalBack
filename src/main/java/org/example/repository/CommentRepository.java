package org.example.repository;

import org.example.entity.Article;
import org.example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);
    void deleteByArticle(Article article);
}