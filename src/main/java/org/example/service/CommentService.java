package org.example.service;

import org.example.dto.CommentDTO;
import org.example.exception.CommentNotFoundException;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO) throws CommentNotFoundException;
    CommentDTO getCommentById(Long id) throws CommentNotFoundException;
    List<CommentDTO> getCommentsByArticleId(Long articleId);
    void deleteComment(Long id) throws CommentNotFoundException;
}