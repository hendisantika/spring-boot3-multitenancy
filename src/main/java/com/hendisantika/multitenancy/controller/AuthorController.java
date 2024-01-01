package com.hendisantika.multitenancy.controller;

import com.hendisantika.multitenancy.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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

    private final AuthorService service;
}
