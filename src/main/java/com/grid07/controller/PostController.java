package com.grid07.controller;

import com.grid07.entity.Comment;
import com.grid07.entity.Post;
import com.grid07.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //  Create Post
    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.createPost(post);
    }

    //  Add Comment
    @PostMapping("/{id}/comments")
    public Comment comment(@PathVariable Long id,
                           @RequestBody Comment comment) {
        return postService.addComment(id, comment);
    }

    // Like Post
    @PostMapping("/{id}/like")
    public String like(@PathVariable Long id) {
        postService.likePost(id);
        return "Liked";
    }
}