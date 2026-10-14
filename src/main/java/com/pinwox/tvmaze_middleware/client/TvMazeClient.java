package com.pinwox.tvmaze_middleware.client;

import com.pinwox.tvmaze_middleware.dto.TvMazeSearchResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class TvMazeClient {

    private final RestClient restClient;

    public TvMazeClient(RestClient.Builder builder) {
        this.restClient = builder
            .baseUrl("https://api.tvmaze.com")
            .build();
    }

    public List<TvMazeSearchResult> searchShows(String query) {
        return restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/search/shows")
                .queryParam("q", query)
                .build())
            .retrieve()
            .body(new ParameterizedTypeReference<
                List<TvMazeSearchResult>>() {});
    }
}