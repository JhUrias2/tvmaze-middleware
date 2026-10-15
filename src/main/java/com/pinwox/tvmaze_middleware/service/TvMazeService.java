package com.pinwox.tvmaze_middleware.service;

import java.util.List;
import java.util.Map;

import com.pinwox.tvmaze_middleware.repository.CommentRepository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.pinwox.tvmaze_middleware.client.TvMazeClient;
import com.pinwox.tvmaze_middleware.dto.ShowSearchResponse;
import com.pinwox.tvmaze_middleware.dto.TvMazeNetworkDto;
import com.pinwox.tvmaze_middleware.dto.TvMazeSearchResult;
import com.pinwox.tvmaze_middleware.dto.TvMazeShowDto;

import com.pinwox.tvmaze_middleware.model.Comment;

@Service
public class TvMazeService {

    private final TvMazeClient tvMazeClient;
    private final MongoTemplate mongoTemplate;
    private final CommentRepository commentRepository;

    public TvMazeService(
            TvMazeClient tvMazeClient,
            MongoTemplate mongoTemplate,
            CommentRepository commentRepository) {
        this.tvMazeClient = tvMazeClient;
        this.mongoTemplate = mongoTemplate;
        this.commentRepository = commentRepository;
    }

    public List<ShowSearchResponse> searchShows(String query) {

    List<TvMazeSearchResult> results =
            tvMazeClient.searchShows(query);

    return results.stream()
            .map(TvMazeSearchResult::show)
            .map(this::toSearchResponse)
            .toList();
}

    private ShowSearchResponse toSearchResponse(TvMazeShowDto show) {

    List<String> genres = show.genres() == null
            ? List.of()
            : show.genres();

    List<Comment> comments =
            commentRepository.findByShowIdOrderByCreatedAtDesc(show.id());

    return new ShowSearchResponse(
            show.id(),
            show.name(),
            resolveChannel(show.network(), show.webChannel()),
            show.summary(),
            genres,
            comments
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

        Document cachedShow
                = mongoTemplate.findById(showId, Document.class, "shows");

        if (cachedShow != null) {
            cachedShow.remove("_id");
            return addComments(cachedShow, showId);
        }

        Map<String, Object> show = tvMazeClient.getShowById(showId);

        Document document = new Document(show);
        document.put("_id", showId);
        mongoTemplate.save(document, "shows");

        return addComments(show, showId);
    }

    private Map<String, Object> addComments(
            Map<String, Object> show,
            Long showId) {

        show.put(
                "comments",
                commentRepository.findByShowIdOrderByCreatedAtDesc(showId)
        );

        return show;
    }
}
