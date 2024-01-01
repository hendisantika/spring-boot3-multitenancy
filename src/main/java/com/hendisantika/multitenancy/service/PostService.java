package com.hendisantika.multitenancy.service;

import com.hendisantika.multitenancy.entity.Author;
import com.hendisantika.multitenancy.entity.Post;
import com.hendisantika.multitenancy.entity.Tag;
import com.hendisantika.multitenancy.exception.BadRequestException;
import com.hendisantika.multitenancy.exception.DataNotFoundException;
import com.hendisantika.multitenancy.model.PostDTO;
import com.hendisantika.multitenancy.repository.AuthorRepository;
import com.hendisantika.multitenancy.repository.PostRepository;
import com.hendisantika.multitenancy.repository.TagRepository;
import com.hendisantika.multitenancy.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * Time: 07:43
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final ModelMapper modelMapper;

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    private final TagRepository tagRepository;

    @Cacheable(value = "posts")
    public Page<Post> getAllPosts(Pageable pageable, String title) {
        Page<Post> postList;
        if (title == null) {
            postList = postRepository.findAll(pageable);
        } else {
            postList = postRepository.findByTitleContainingIgnoreCase(title, pageable);
        }
        return postList;
    }

    public Post getById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new DataNotFoundException(
                                        MessageFormat.format("Post id {0} not found", String.valueOf(id))));
    }

    public Post createOrUpdate(PostDTO postRequest) {
        Optional<Post> existingPost = postRepository.findById(postRequest.getId());

        if (existingPost.isPresent()) {
            Post postUpdate = existingPost.get();

            postUpdate.setTitle(postRequest.getTitle());
            postUpdate.setBody(postRequest.getBody());
            if (postRequest.getAuthorId() != 0) {
                Optional<Author> author = authorRepository.findById(postRequest.getAuthorId());
                author.ifPresent(postUpdate::setAuthor);
            }

            return postRepository.save(postUpdate);
        } else {
            return postRepository.save(modelMapper.map(postRequest, Post.class));
        }
    }

    public List<Tag> getAllTagsByPostId(Long id) {
        if (!postRepository.existsById(id)) {
            throw new DataNotFoundException(
                    MessageFormat.format("Post id {0} not found", String.valueOf(id)));
        }

        return postRepository.findById(id).get().getTagList();
    }

    public Tag addTag(Long postId, Tag tagRequest) {
        return postRepository
                .findById(postId)
                .map(
                        post -> {
                            Optional<Tag> existingTag = tagRepository.findById(tagRequest.getId());
                            if (tagRequest.getId() != 0) {
                                if (existingTag.isPresent()) {
                                    post.addTag(existingTag.get());
                                    postRepository.save(post);
                                    return existingTag.get();
                                } else {
                                    throw new DataNotFoundException(
                                            MessageFormat.format(
                                                    "Tag id {0} not found", String.valueOf(tagRequest.getId())));
                                }
                            } else {
                                // create new tag
                                post.addTag(tagRequest);
                                return tagRepository.save(tagRequest);
                            }
                        })
                .orElseThrow(
                        () ->
                                new DataNotFoundException(
                                        MessageFormat.format("Post id {0} not found", String.valueOf(postId))));
    }

    public void deleteTagFromPost(Long postId, Long tagId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            post.get().removeTag(tagId);
            postRepository.save(post.get());
        } else {
            throw new BadRequestException(Translator.toLocale("DELETE_ERROR_PLEASE_CHECK_ID"));
        }
    }

    public void deleteById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new BadRequestException(Translator.toLocale("DELETE_ERROR_PLEASE_CHECK_ID"));
        }
    }
}
