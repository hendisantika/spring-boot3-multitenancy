package com.hendisantika.multitenancy.controller;

import com.hendisantika.multitenancy.entity.Tag;
import com.hendisantika.multitenancy.service.TagService;
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
 * Time: 05:06
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/v1/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> list = tagService.getAllTags();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/v1/tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") Long id) {
        Tag entity = tagService.getById(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping("/v1/tags")
    public ResponseEntity<Tag> createOrUpdate(@RequestBody Tag Tag) {
        Tag updated = tagService.createOrUpdate(Tag);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/v1/tags/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        tagService.deleteById(id);
    }
}
