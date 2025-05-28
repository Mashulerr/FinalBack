package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ArticleDTO;
import org.example.dto.UserDTO;
import org.example.entity.Article;
import org.example.entity.Reaction;
import org.example.entity.User;
import org.example.exception.ArticleNotFoundException;
import org.example.exception.InvalidNewsException;
import org.example.exception.NewsNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.FavoriteArticleRepository;
import org.example.repository.ReactionRepository;
import org.example.service.ArticleService;
import org.example.service.FavoriteArticleService;
import org.example.service.UserService;
import org.example.utils.ArticleMapper;
import org.example.utils.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.UserUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final FavoriteArticleRepository favoriteArticleRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final UserService userService;
    private final FavoriteArticleService favoriteArticleService;

    @Override
    public List<ArticleDTO> getAllArticles() {
        // Получаем текущего пользователя (может быть null если не аутентифицирован)
        Long currentUserId = UserUtils.getCurrentUser_id();
        User currentUser = currentUserId != null ?
                userService.getUserEntityById(currentUserId) :
                null;

        return articleRepository.findAll().stream()
                .map(article -> {
                    User author = article.getUser();
                    ArticleDTO dto = ArticleMapper.convertToDto(article, author);

                    // Проверяем, находится ли статья в избранном у текущего пользователя
                    if (currentUser != null) {
                        try {
                            boolean isFavorite = favoriteArticleService.existsByUserIdAndArticleId(
                                    currentUser.getId(),
                                    article.getId()
                            );
                            dto.setFavorite(isFavorite);
                        } catch (Exception e) {
                            // Логирование ошибки
                            dto.setFavorite(false);
                        }
                    } else {
                        dto.setFavorite(false);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public Article getArticleEntityById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + id));
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

        User author = article.getUser();
        ArticleDTO dto = ArticleMapper.convertToDto(article, author);

        // Проверка избранного с обработкой возможных ошибок
        try {
            Long currentUserId = UserUtils.getCurrentUser_id();
            if (currentUserId != null) {
                boolean isFavorite = favoriteArticleService.existsByUserIdAndArticleId(currentUserId, id);
                dto.setFavorite(isFavorite);
            } else {
                dto.setFavorite(false);
            }
        } catch (Exception e) {
            // Логирование ошибки
            dto.setFavorite(false);
        }

        return dto;
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) throws UnauthorizedException {

        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }


        if (articleDTO.getTitle() == null || articleDTO.getTitle().trim().isEmpty()) {
            throw new InvalidNewsException("Article title cannot be empty");
        }


        UserDTO userDTO = userService.getUserById(userId);
        User author = UserMapper.convertToEntity(userDTO);


        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUser(author);


        article.setLikes(0);
        article.setDislikes(0);
        article.setComments(new ArrayList<>());
        article.setReactions(new ArrayList<>());


        Article savedArticle = articleRepository.save(article);


        ArticleDTO responseDTO = new ArticleDTO();
        responseDTO.setId(savedArticle.getId());
        responseDTO.setTitle(savedArticle.getTitle());
        responseDTO.setContent(savedArticle.getContent());
        responseDTO.setUsername(author.getUsername());
        responseDTO.setPhotoUrl(author.getPhotoUrl());
        responseDTO.setLikes(0);
        responseDTO.setDislikes(0);
        responseDTO.setComments(new ArrayList<>());

        return responseDTO;
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Article not found!"));


        if (dto.getTitle() != null && !dto.getTitle().isBlank() && !dto.getTitle().equals("string")) {
            article.setTitle(dto.getTitle());
        }


        if (dto.getContent() != null && !dto.getContent().isBlank() && !dto.getContent().equals("string")) {
            article.setContent(dto.getContent());
        }


        List<Reaction> reactions = reactionRepository.findByArticleId(id);
        long likesCount = reactions.stream().filter(r -> "like".equals(r.getType())).count();
        long dislikesCount = reactions.stream().filter(r -> "dislike".equals(r.getType())).count();

        article.setLikes((int) likesCount);
        article.setDislikes((int) dislikesCount);

        User user = article.getUser();
        Article updatedArticle = articleRepository.save(article);
        return ArticleMapper.convertToDto(updatedArticle, user);
    }


    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + id));


        favoriteArticleRepository.deleteByArticle(article);
        reactionRepository.deleteByArticle(article);
        commentRepository.deleteByArticle(article);

        articleRepository.delete(article);
    }
}