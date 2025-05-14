package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.UserUtils;
import org.example.dto.ArticleDTO;
import org.example.exception.UnauthorizedException;
import org.example.service.FavoriteArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteArticleController {
    private final FavoriteArticleService favoriteArticleService;

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long articleId) {
        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        favoriteArticleService.addToFavorites(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long articleId) {
        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        favoriteArticleService.removeFromFavorites(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getUserFavorites() {
        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        return ResponseEntity.ok(favoriteArticleService.getUserFavorites(userId));
    }
}