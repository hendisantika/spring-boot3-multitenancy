package com.hendisantika.multitenancy.controller;

import com.hendisantika.multitenancy.entity.Tag;
import com.hendisantika.multitenancy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/2/24
 * Time: 05:04
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PostTagController {

    private final PostService postService;

    @GetMapping("/v1/posts/{id}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByPostId(@PathVariable(value = "id") Long id) {
        List<Tag> tagList = postService.getAllTagsByPostId(id);
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    @PostMapping("/v1/posts/{id}/tags")
    public ResponseEntity<Tag> addTag(@PathVariable("id") Long id, @RequestBody Tag tagRequest) {
        Tag updated = postService.addTag(id, tagRequest);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
