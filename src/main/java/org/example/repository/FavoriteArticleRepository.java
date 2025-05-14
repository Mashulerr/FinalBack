package org.example.repository;

import org.example.entity.Article;
import org.example.entity.FavoriteArticle;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticle, Long> {


    boolean existsByUserAndArticle(User user, Article article);
    List<FavoriteArticle> findByUser(User user);

    void deleteByArticle(Article article);
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
    void deleteByUserAndArticle(User user, Article article);

    @Query("SELECT fa.article.id FROM FavoriteArticle fa WHERE fa.user.id = :userId")
    Set<Long> getUserFavorites(@Param("userId") Long userId);
}