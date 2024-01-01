package com.hendisantika.multitenancy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:23
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Base implements Serializable {

    private static final long serialVersionUID = 7156526087883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties("postList")
    private Author author;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties("postList")
    private List<Tag> tagList;

    public void addTag(Tag tag) {
        this.tagList.add(tag);
    }

    public void removeTag(long tagId) {
        Optional<Tag> tag = this.tagList.stream().filter(t -> t.getId() == tagId).findFirst();
        tag.ifPresent(value -> this.tagList.remove(value));
    }
}
