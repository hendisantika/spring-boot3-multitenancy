package com.hendisantika.multitenancy.service;

import com.hendisantika.multitenancy.SpringBoot3MultitenancyApplication;
import com.hendisantika.multitenancy.entity.Tag;
import com.hendisantika.multitenancy.exception.DataNotFoundException;
import com.hendisantika.multitenancy.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:46
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        SpringBoot3MultitenancyApplication.defaultProperties.setProperty("lang", "fr");

        List<Tag> tagList = tagRepository.findAll();
        return tagList;
    }

    public Tag getById(Long id) {
        return tagRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new DataNotFoundException(
                                        MessageFormat.format("Tag id {0} not found", String.valueOf(id))));
    }
}
