package com.hendisantika.multitenancy.config.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/2/24
 * Time: 04:59
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableConfigurationProperties(DbConfigProperty.class)
@RequiredArgsConstructor
public class DbConfig {

    private final DbConfigProperty dbConfigProperty;

    @Bean
    public DataSource dataSource() {
        CustomRoutingDataSource dataSource = new CustomRoutingDataSource();
        dataSource.setTargetDataSources(dbConfigProperty.createTargetDataSources());
        return dataSource;
    }
}
