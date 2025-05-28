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

    private void adjustReactionCounters(Article article, ReactionType type, int delta) {
        if (type == ReactionType.LIKE) {
            article.setLikes(article.getLikes() + delta);
        } else if (type == ReactionType.DISLIKE) {
            article.setDislikes(article.getDislikes() + delta);
        }
    }

    @Override
    public ReactionDTO createReaction(ReactionDTO reactionDTO) throws ArticleNotFoundException, UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();

        ReactionType newType;
        try {
            newType = ReactionType.fromValue(reactionDTO.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }


        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found."));

        Long articleId = reactionDTO.getArticleId();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found."));

        // Получаем тип реакции из DTO, очищаем пробелы и приводим к верхнему регистру
        String rawType = reactionDTO.getType();
        if (rawType == null || rawType.trim().isEmpty()) {
            throw new IllegalArgumentException("Reaction type must be specified");
        }



        Optional<Reaction> existingReactionOpt = reactionRepository.findByUserAndArticle(user, article);

        if (existingReactionOpt.isPresent()) {
            Reaction existingReaction = existingReactionOpt.get();

            // Если тип реакции не изменился — возвращаем существующую
            if (existingReaction.getType().equals(newType)) {
                return ReactionMapper.convertToDto(existingReaction);
            }

            // Корректируем счетчики лайков/дизлайков
            adjustReactionCounters(article, existingReaction.getType(), -1);
            adjustReactionCounters(article, newType, 1);

            existingReaction.setType(newType);

            articleRepository.save(article);
            Reaction updatedReaction = reactionRepository.save(existingReaction);
            return ReactionMapper.convertToDto(updatedReaction);
        }

        // Создаем новую реакцию
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


        Article article = reaction.getArticle();
        adjustReactionCounters(article, reaction.getType(), -1);
        articleRepository.save(article);

        reactionRepository.delete(reaction);
    }

    // Остальные методы остаются без изменений
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