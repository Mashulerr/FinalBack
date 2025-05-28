package org.example.service;

import org.example.dto.ReactionDTO;
import org.example.entity.Reaction;
import org.example.exception.ArticleNotFoundException;

import java.util.List;

public interface ReactionService {
    ReactionDTO createReaction(ReactionDTO reactionDTO) throws ArticleNotFoundException;
    ReactionDTO getReactionById(Long id) throws ArticleNotFoundException;
    List<ReactionDTO> getReactionsByArticleId(Long articleId);
    void deleteReaction(Long id) throws ArticleNotFoundException;
    List<ReactionDTO> getReactionsByUserId(Long userId);

}
