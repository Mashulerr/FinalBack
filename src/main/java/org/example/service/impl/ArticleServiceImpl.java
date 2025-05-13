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
    private final UserService userService;

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> {
                    User user = article.getUser();
                    return ArticleMapper.convertToDto(article, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(article -> {
                    User user = article.getUser();
                    return ArticleMapper.convertToDto(article, user);
                })
                .orElseThrow(() -> new ArticleNotFoundException("Article not found!"));
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) throws UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();
        if (userId == null) {
            throw new UnauthorizedException("User  is not authenticated.");
        }

        UserDTO userDTO = userService.getUserById(userId);
        User user = UserMapper.convertToEntity(userDTO);

        Article article = ArticleMapper.convertToEntity(articleDTO, user);
        Article savedArticle = articleRepository.save(article);

        return ArticleMapper.convertToDto(savedArticle, user);
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Article not found!"));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());

        User user = article.getUser ();
        return ArticleMapper.convertToDto(articleRepository.save(article), user);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}