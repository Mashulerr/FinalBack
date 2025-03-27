package org.example.service.impl;

import org.example.dto.CommentDTO;
import org.example.entity.Article;
import org.example.entity.Comment;
import org.example.entity.User;
import org.example.exception.CommentNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.UserRepository;
import org.example.service.CommentService;
import org.example.utils.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.UserUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) throws UnauthorizedException {
        Long userId = UserUtils.getCurrentUser_id();

        if (userId == null) {
            throw new UnauthorizedException("User  is not authenticated.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User  not found."));

        Long articleId = commentDTO.getArticleId();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found."));

        Comment comment = CommentMapper.convertToEntity(commentDTO);
        comment.setUser(user);
        comment.setArticle(article);
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.convertToDto(savedComment);
    }


    @Override
    public CommentDTO getCommentById(Long id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found!"));
        return CommentMapper.convertToDto(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream()
                .map(CommentMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found!"));
        commentRepository.delete(comment);
    }
}

