
package com.pinwox.tvmaze_middleware.repository;

import com.pinwox.tvmaze_middleware.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByShowIdOrderByCreatedAtDesc(Long showId);
}
