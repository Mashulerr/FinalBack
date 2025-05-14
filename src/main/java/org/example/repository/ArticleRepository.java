package org.example.repository;

import org.example.entity.Article;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Modifying
    @Query("DELETE FROM Article a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);
}
