package com.pinwox.tvmaze_middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinwox.tvmaze_middleware.dto.ShowSearchResponse;
import com.pinwox.tvmaze_middleware.service.TvMazeService;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final TvMazeService tvMazeService;

    public ShowController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShowSearchResponse>> searchShows(
        @RequestParam("q") String query
    ) {
        if (query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            tvMazeService.searchShows(query)
        );
    }
}