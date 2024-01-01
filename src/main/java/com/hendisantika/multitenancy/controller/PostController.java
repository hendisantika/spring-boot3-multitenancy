package com.hendisantika.multitenancy.controller;

import com.hendisantika.multitenancy.entity.Post;
import com.hendisantika.multitenancy.model.PostDTO;
import com.hendisantika.multitenancy.service.PostService;
import com.hendisantika.multitenancy.util.PageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/2/24
 * Time: 05:03
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/v1/posts")
    public ResponseEntity<Page<Post>> getAllPosts(
            Pageable pageable, @RequestParam(required = false) String title) {

        Page<Post> list = postService.getAllPosts(PageUtils.pageable(pageable), title);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/v1/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        Post entity = postService.getById(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping("/v1/posts")
    public ResponseEntity<Post> createOrUpdate(@Valid @RequestBody PostDTO postDTO) {
        Post updated = postService.createOrUpdate(postDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/v1/posts/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        postService.deleteById(id);
    }
}
