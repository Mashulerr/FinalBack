package org.example.repository;

import org.example.entity.Article;
import org.example.entity.Reaction;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    void deleteByArticle(Article article);

    List<Reaction> findByArticleId(Long articleId);
    List<Reaction> findByUserId(Long userId);

    Optional<Reaction> findByUserAndArticle(User user, Article article);

    @Query("SELECT r FROM Reaction r WHERE r.user.id = :userId AND r.article.id = :articleId")
    List<Reaction> findAllByUserIdAndArticleId(@Param("userId") Long userId,
                                               @Param("articleId") Long articleId);

    @Modifying
    @Query("DELETE FROM Reaction r WHERE r.user.id = :userId AND r.article.id = :articleId AND r.id != :keepId")
    void deleteDuplicates(@Param("userId") Long userId,
                          @Param("articleId") Long articleId,
                          @Param("keepId") Long keepId);
}
