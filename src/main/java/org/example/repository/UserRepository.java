package org.example.repository;


import org.example.entity.Article;
import org.example.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM user WHERE id = ?1", nativeQuery = true)
    void deleteUserById(long id);


    public interface ArticleRepository extends JpaRepository<Article, Long> {
    }
}