package com.grid07.service;

import com.grid07.entity.Comment;
import com.grid07.entity.Post;
import com.grid07.repository.CommentRepository;
import com.grid07.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;





@Service
public class PostService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final RedisService redisService;

    // Constructor Injection (Best Practice)
    public PostService(PostRepository postRepo,
                       CommentRepository commentRepo,
                       RedisService redisService) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.redisService = redisService;
    }

    //  Create Post
    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return postRepo.save(post);
    }

    //  Add Comment
    public Comment addComment(Long postId, Comment comment) {

        //  Depth limit
        if (comment.getDepthLevel() > 20) {
            throw new RuntimeException("Max depth exceeded");
        }

        //  Bot control (assume botId < 1000)
        if (comment.getAuthorId() < 1000) {

            if (!redisService.allowBot(postId)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
            }

            redisService.updateVirality(postId, "BOT");

        } else {
            redisService.updateVirality(postId, "COMMENT");
        }

        comment.setPostId(postId);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepo.save(comment);
    }

    //  Like Post
    public void likePost(Long postId) {
        redisService.updateVirality(postId, "LIKE");
    }
}