package com.hendisantika.multitenancy.controller;

import com.hendisantika.multitenancy.entity.Author;
import com.hendisantika.multitenancy.service.AuthorService;
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
 * Time: 05:01
 * To change this template use File | Settings | File Templates.
 */

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/v1/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> list = authorService.getAllAuthors();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/v1/authors/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) {
        Author entity = authorService.getById(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping("/v1/authors")
    public ResponseEntity<Author> createOrUpdate(@RequestBody Author Author) {
        Author updated = authorService.createOrUpdate(Author);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
