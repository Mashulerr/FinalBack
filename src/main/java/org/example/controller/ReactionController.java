package org.example.controller;

import org.example.UserUtils;
import org.example.dto.ReactionDTO;
import org.example.exception.ArticleNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }



    @PostMapping
    public ResponseEntity<ReactionDTO> createReaction(@RequestBody ReactionDTO reactionDTO) {
        ReactionDTO createdReaction = reactionService.createReaction(reactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }

    @GetMapping("/user/{userId}/article/{articleId}")
    public ResponseEntity<ReactionDTO> getReactionByUserAndArticle(
            @PathVariable Long userId,
            @PathVariable Long articleId) {
        try {
            ReactionDTO reaction = reactionService.getReactionByUserAndArticle(userId, articleId);
            return ResponseEntity.ok(reaction);
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/current-user/article/{articleId}")
    public ResponseEntity<ReactionDTO> getCurrentUserReactionForArticle(
            @PathVariable Long articleId) {
        try {
            long userId = UserUtils.getCurrentUser_id();
            ReactionDTO reaction = reactionService.getReactionByUserAndArticle(userId, articleId);
            return ResponseEntity.ok(reaction);
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionDTO> getReactionById(@PathVariable Long id) {
        ReactionDTO reactionDTO = reactionService.getReactionById(id);
        return ResponseEntity.ok(reactionDTO);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsByArticleId(@PathVariable Long articleId) {
        List<ReactionDTO> reactions = reactionService.getReactionsByArticleId(articleId);
        return ResponseEntity.ok(reactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}
