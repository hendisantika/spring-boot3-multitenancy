package com.hendisantika.multitenancy.service;

import com.hendisantika.multitenancy.SpringBoot3MultitenancyApplication;
import com.hendisantika.multitenancy.entity.Author;
import com.hendisantika.multitenancy.exception.BadRequestException;
import com.hendisantika.multitenancy.exception.DataNotFoundException;
import com.hendisantika.multitenancy.repository.AuthorRepository;
import com.hendisantika.multitenancy.util.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:41
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        SpringBoot3MultitenancyApplication.defaultProperties.setProperty("lang", "en");

        List<Author> authorList = authorRepository.findAll();
        return authorList;
    }

    public Author getById(Long id) {
        return authorRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new DataNotFoundException(
                                        MessageFormat.format("Author id {0} not found", String.valueOf(id))));
    }

    public Author createOrUpdate(Author authorRequest) {
        Optional<Author> existingAuthor = authorRepository.findById(authorRequest.getId());

        if (existingAuthor.isPresent()) {
            Author authorUpdate = existingAuthor.get();

            authorUpdate.setName(authorRequest.getName());

            return authorRepository.save(authorUpdate);
        } else {
            return authorRepository.save(authorRequest);
        }
    }

    public void deleteById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            authorRepository.deleteById(id);
        } else {
            throw new BadRequestException(Translator.toLocale("DELETE_ERROR_PLEASE_CHECK_ID"));
        }
    }
}
