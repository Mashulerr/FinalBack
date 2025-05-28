package org.example.service.impl;


import org.example.UserUtils;
import org.example.dto.ReactionDTO;
import org.example.entity.Article;
import org.example.entity.Reaction;
import org.example.entity.User;
import org.example.exception.ArticleNotFoundException;
import org.example.exception.DuplicateReactionException;
import org.example.exception.UnauthorizedException;
import org.example.repository.ArticleRepository;
import org.example.repository.ReactionRepository;
import org.example.repository.UserRepository;
import org.example.service.ReactionService;
import org.example.utils.ReactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository,
                               ArticleRepository articleRepository,
                               UserRepository userRepository) {
        this.reactionRepository = reactionRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReactionDTO createReaction(ReactionDTO reactionDTO) throws ArticleNotFoundException, UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();

        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found."));

        Long articleId = reactionDTO.getArticleId();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found."));

        Optional<Reaction> existingReaction = reactionRepository.findByUserAndArticle(user, article);

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            if (reaction.getType().equals(reactionDTO.getType())) {
                return ReactionMapper.convertToDto(reaction);
            }

            reaction.setType(reactionDTO.getType());
            Reaction updatedReaction = reactionRepository.save(reaction);
            return ReactionMapper.convertToDto(updatedReaction);
        }

        Reaction reaction = new Reaction();
        reaction.setType(reactionDTO.getType());
        reaction.setArticle(article);
        reaction.setUser(user);

        Reaction savedReaction = reactionRepository.save(reaction);
        return ReactionMapper.convertToDto(savedReaction);
    }

    @Override
    public void deleteReaction(Long reactionId) throws ArticleNotFoundException, UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();

        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }

        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ArticleNotFoundException("Reaction not found!"));

        if (reaction.getUser().getId() != userId) {
            throw new UnauthorizedException("You can only delete your own reactions");
        }

        reactionRepository.delete(reaction);
    }

    @Override
    public ReactionDTO getReactionById(Long id) throws ArticleNotFoundException {
        Reaction reaction = reactionRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Reaction not found!"));
        return ReactionMapper.convertToDto(reaction);
    }

    @Override
    public List<ReactionDTO> getReactionsByArticleId(Long articleId) {
        List<Reaction> reactions = reactionRepository.findByArticleId(articleId);
        return reactions.stream()
                .map(ReactionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReactionDTO> getReactionsByUserId(Long userId) {
        List<Reaction> reactions = reactionRepository.findByUserId(userId);
        return reactions.stream()
                .map(ReactionMapper::convertToDto)
                .collect(Collectors.toList());
    }
}