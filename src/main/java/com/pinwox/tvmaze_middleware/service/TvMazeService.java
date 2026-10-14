package com.pinwox.tvmaze_middleware.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pinwox.tvmaze_middleware.client.TvMazeClient;
import com.pinwox.tvmaze_middleware.dto.ShowSearchResponse;
import com.pinwox.tvmaze_middleware.dto.TvMazeNetworkDto;
import com.pinwox.tvmaze_middleware.dto.TvMazeSearchResult;
import com.pinwox.tvmaze_middleware.dto.TvMazeShowDto;

@Service
public class TvMazeService {

    private final TvMazeClient tvMazeClient;

    public TvMazeService(TvMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    public List<ShowSearchResponse> searchShows(String query) {
        return tvMazeClient.searchShows(query)
            .stream()
            .map(TvMazeSearchResult::show)
            .map(this::toSearchResponse)
            .toList();
    }

    private ShowSearchResponse toSearchResponse(
        TvMazeShowDto show
    ) {
        return new ShowSearchResponse(
            show.id(),
            show.name(),
            resolveChannel(show.network(), show.webChannel()),
            show.summary(),
            show.genres() == null
                ? List.of()
                : show.genres()
        );
    }

    private String resolveChannel(
        TvMazeNetworkDto network,
        TvMazeNetworkDto webChannel
    ) {
        if (network != null && network.name() != null) {
            return network.name();
        }

        if (webChannel != null) {
            return webChannel.name();
        }

        return null;
    }

    public Map<String, Object> getShowById(Long showId) {
    return tvMazeClient.getShowById(showId);
}
}