package com.pinwox.tvmaze_middleware.dto;

import java.util.List;

public record TvMazeShowDto(
        Long id,
        String name,
        String summary,
        List<String> genres,
        TvMazeNetworkDto network,
        TvMazeNetworkDto webChannel
) {}
