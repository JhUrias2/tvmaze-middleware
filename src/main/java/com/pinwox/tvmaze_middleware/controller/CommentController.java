package com.pinwox.tvmaze_middleware.controller;

import java.util.List;

import com.pinwox.tvmaze_middleware.dto.CreateCommentRequest;
import com.pinwox.tvmaze_middleware.model.Comment;
import com.pinwox.tvmaze_middleware.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shows/{showId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public java.util.List<Comment> getComments(
            @PathVariable Long showId) {

        return commentService.getCommentsByShowId(showId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(
            @PathVariable Long showId,
            @Valid @RequestBody CreateCommentRequest request) {

        return commentService.createComment(showId, request);
    }
}
