package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ArticleDTO;
import org.example.entity.Article;
import org.example.entity.FavoriteArticle;
import org.example.entity.User;
import org.example.exception.AlreadyExistsException;
import org.example.exception.ArticleNotFoundException;
import org.example.repository.ArticleRepository;
import org.example.repository.FavoriteArticleRepository;
import org.example.service.ArticleService;
import org.example.service.FavoriteArticleService;
import org.example.service.UserService;
import org.example.utils.ArticleMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteArticleServiceImpl implements FavoriteArticleService {
    private final FavoriteArticleRepository favoriteArticleRepository;
    private final UserService userService;
    private final ArticleRepository articleRepository; // Заменяем ArticleService на репозиторий

    @Override
    public void addToFavorites(Long userId, Long articleId) {
        User user = userService.getUserEntityById(userId);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

        if (favoriteArticleRepository.existsByUserAndArticle(user, article)) {
            throw new AlreadyExistsException("Article already in favorites");
        }

        FavoriteArticle favorite = new FavoriteArticle();
        favorite.setUser(user);
        favorite.setArticle(article);
        favoriteArticleRepository.save(favorite);
    }

    @Override
    public boolean isArticleInFavorites(Long userId, Long articleId) {
        return favoriteArticleRepository.existsByUserIdAndArticleId(userId, articleId);
    }

    @Override
    public List<ArticleDTO> getUserFavorites(Long userId) {
        User user = userService.getUserEntityById(userId);

        return favoriteArticleRepository.findByUser(user).stream()
                .map(fav -> {
                    Article article = fav.getArticle();
                    User author = article.getUser();
                    ArticleDTO dto = ArticleMapper.convertToDto(article, author);

                    // Явно устанавливаем флаг избранности в true
                    dto.setFavorite(true);  // Все статьи в этом списке по определению избранные

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long userId, Long articleId) {
        User user = userService.getUserEntityById(userId);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
        favoriteArticleRepository.deleteByUserAndArticle(user, article);
    }




    @Override
    public boolean existsByUserIdAndArticleId(Long userId, Long articleId) {
        return favoriteArticleRepository.existsByUserIdAndArticleId(userId, articleId);
    }
}