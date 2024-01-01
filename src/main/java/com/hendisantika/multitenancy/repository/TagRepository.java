package com.hendisantika.multitenancy.repository;

import com.hendisantika.multitenancy.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:28
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
