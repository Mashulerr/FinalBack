package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ArticleDTO;
import org.example.dto.UserDTO;
import org.example.entity.Article;
import org.example.entity.User;
import org.example.exception.ArticleNotFoundException;
import org.example.exception.NewsNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.repository.ArticleRepository;
import org.example.repository.UserRepository;
import org.example.service.ArticleService;
import org.example.service.UserService;
import org.example.utils.ArticleMapper;
import org.example.utils.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.UserUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService; // Изменено на UserService

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> {
                    User user = article.getUser(); // Получаем пользователя из статьи
                    return ArticleMapper.convertToDto(article, user); // Передаем пользователя
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(article -> {
                    User user = article.getUser (); // Получаем пользователя из статьи
                    return ArticleMapper.convertToDto(article, user); // Преобразуем статью в DTO
                })
                .orElseThrow(() -> new ArticleNotFoundException("Article not found!")); // Исключение, если статья не найдена
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) throws UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id(); // Предполагается, что у вас есть метод для получения текущего пользователя

        if (userId == null) {
            throw new UnauthorizedException("User  is not authenticated.");
        }

        UserDTO userDTO = userService.getUserById(userId);
        User user = UserMapper.convertToEntity(userDTO); // Преобразуем UserDTO в User

        Article article = ArticleMapper.convertToEntity(articleDTO, user); // Теперь передаем пользователя
        Article savedArticle = articleRepository.save(article);

        return ArticleMapper.convertToDto(savedArticle, user); // Передаем пользователя
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Article not found!"));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());

        User user = article.getUser (); // Получаем пользователя из статьи
        return ArticleMapper.convertToDto(articleRepository.save(article), user); // Передаем пользователя
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}