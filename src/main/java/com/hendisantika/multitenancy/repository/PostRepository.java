package com.hendisantika.multitenancy.repository;

import com.hendisantika.multitenancy.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:26
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
