package org.example.service.impl;


import org.example.UserUtils;
import org.example.dto.ReactionDTO;
import org.example.entity.Article;
import org.example.entity.Reaction;
import org.example.entity.ReactionType;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



import java.util.Objects;


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
    @Transactional
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

        ReactionType newType = ReactionType.fromValue(reactionDTO.getType());

        Optional<Reaction> existingReactionOpt = reactionRepository.findByUserAndArticle(user, article);

        if (existingReactionOpt.isPresent()) {
            Reaction existingReaction = existingReactionOpt.get();
            if (existingReaction.getType() == newType) {
                return ReactionMapper.convertToDto(existingReaction);
            }

            adjustReactionCounters(article, existingReaction.getType(), -1);
            adjustReactionCounters(article, newType, 1);

            existingReaction.setType(newType);
            Reaction updatedReaction = reactionRepository.save(existingReaction);
            articleRepository.save(article);
            return ReactionMapper.convertToDto(updatedReaction);
        }

        Reaction reaction = new Reaction();
        reaction.setType(newType);
        reaction.setArticle(article);
        reaction.setUser(user);

        adjustReactionCounters(article, newType, 1);
        articleRepository.save(article);

        Reaction savedReaction = reactionRepository.save(reaction);
        return ReactionMapper.convertToDto(savedReaction);
    }

    @Override
    @Transactional
    public void deleteReaction(Long reactionId) throws ArticleNotFoundException, UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }

        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ArticleNotFoundException("Reaction not found!"));

        if (!Objects.equals(reaction.getUser().getId(), userId)) {
            throw new UnauthorizedException("You can only delete your own reactions");
        }

        Article article = reaction.getArticle();
        adjustReactionCounters(article, reaction.getType(), -1);

        reactionRepository.delete(reaction);
        articleRepository.save(article);
    }

    private void adjustReactionCounters(Article article, ReactionType type, int delta) {
        Objects.requireNonNull(article, "Article must not be null");
        Objects.requireNonNull(type, "ReactionType must not be null");

        switch (type) {
            case LIKE:
                int newLikes = article.getLikes() + delta;
                article.setLikes(Math.max(newLikes, 0));
                break;
            case DISLIKE:
                int newDislikes = article.getDislikes() + delta;
                article.setDislikes(Math.max(newDislikes, 0));
                break;
            default:
                throw new IllegalArgumentException("Unknown ReactionType: " + type);
        }
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



    @Override
    @Transactional
    public ReactionDTO getReactionByUserAndArticle(Long userId, Long articleId) throws ArticleNotFoundException {
        List<Reaction> reactions = reactionRepository.findAllByUserIdAndArticleId(userId, articleId);

        if (reactions.isEmpty()) {
            throw new ArticleNotFoundException(
                    String.format("Reaction not found for user %d and article %d", userId, articleId));
        }

        if (reactions.size() > 1) {
            Reaction reactionToKeep = reactions.get(0);
            reactionRepository.deleteDuplicates(userId, articleId, reactionToKeep.getId());
        }

        return ReactionMapper.convertToDto(reactions.get(0));
    }
}