package com.hendisantika.multitenancy.config.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/2/24
 * Time: 05:00
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "db")
public class DbConfigProperty {

    private Map<String, DbConfigDataSource> configurations = new HashMap<>();

    public Map<Object, Object> createTargetDataSources() {
        Map<Object, Object> result = new HashMap<>();
        configurations.forEach((key, value) -> result.put(key, value.createDataSource()));
        return result;
    }
}
