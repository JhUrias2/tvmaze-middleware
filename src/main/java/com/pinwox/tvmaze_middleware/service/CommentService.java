package com.pinwox.tvmaze_middleware.service;

import java.util.List;

import com.pinwox.tvmaze_middleware.dto.CreateCommentRequest;
import com.pinwox.tvmaze_middleware.model.Comment;
import com.pinwox.tvmaze_middleware.repository.CommentRepository;

import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TvMazeService tvMazeService;

    public CommentService(
            CommentRepository commentRepository,
            TvMazeService tvMazeService) {
        this.commentRepository = commentRepository;
        this.tvMazeService = tvMazeService;
    }

    public List<Comment> getCommentsByShowId(Long showId) {
        return commentRepository.findByShowIdOrderByCreatedAtDesc(showId);
    }

    public Comment createComment(
            Long showId,
            CreateCommentRequest request) {

        // Verificar que el programa exista
        tvMazeService.getShowById(showId);

        // Construir la reseña
        Comment comment = new Comment(
                showId,
                request.getComment().trim(),
                request.getRating()
        );

        // Guardar en MongoDB
        return commentRepository.save(comment);
    }

    
}
