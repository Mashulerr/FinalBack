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

        // Проверяем существующие реакции пользователя на статью
        List<Reaction> existingReactions = reactionRepository.findByUserIdAndArticleId(userId, articleId);

        if (!existingReactions.isEmpty()) {
            Reaction existingReaction = existingReactions.get(0);
            String oldType = existingReaction.getType();
            String newType = reactionDTO.getType();

            // Если тип реакции не изменился, просто возвращаем существующую реакцию
            if (oldType.equals(newType)) {
                return ReactionMapper.convertToDto(existingReaction);
            }

            // Обновляем счетчики в статье
            updateArticleCounters(article, oldType, newType);

            // Обновляем тип реакции
            existingReaction.setType(newType);
            Reaction updatedReaction = reactionRepository.save(existingReaction);
            articleRepository.save(article);

            return ReactionMapper.convertToDto(updatedReaction);
        }

        // Создаем новую реакцию
        Reaction reaction = ReactionMapper.convertToEntity(reactionDTO);
        reaction.setUser(user);
        reaction.setArticle(article);

        // Обновляем счетчики в статье
        updateArticleCounters(article, null, reactionDTO.getType());

        Reaction savedReaction = reactionRepository.save(reaction);
        articleRepository.save(article);

        return ReactionMapper.convertToDto(savedReaction);
    }

    @Override
    public void deleteReaction(Long id) throws ArticleNotFoundException {
        Reaction reaction = reactionRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Reaction not found!"));

        Article article = reaction.getArticle();

        // Уменьшаем счетчик в статье
        updateArticleCounters(article, reaction.getType(), null);

        reactionRepository.delete(reaction);
        articleRepository.save(article);
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

    // Вспомогательный метод для обновления счетчиков в статье
    private void updateArticleCounters(Article article, String oldType, String newType) {
        if (oldType != null) {
            if ("like".equals(oldType)) {
                article.setLikes(article.getLikes() - 1);
            } else if ("dislike".equals(oldType)) {
                article.setDislikes(article.getDislikes() - 1);
            }
        }

        if (newType != null) {
            if ("like".equals(newType)) {
                article.setLikes(article.getLikes() + 1);
            } else if ("dislike".equals(newType)) {
                article.setDislikes(article.getDislikes() + 1);
            }
        }
    }
}