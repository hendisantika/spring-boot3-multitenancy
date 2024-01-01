package com.hendisantika.multitenancy.config.db;

import lombok.Setter;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

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
@Setter
public class DbConfigDataSource {

    private String url;
    private String username;
    private String driver;
    private String password;

    public DataSource createDataSource() {
        // DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // dataSource.setDriverClassName(driver);
        // dataSource.setUrl(url);
        // dataSource.setUsername(username);
        // dataSource.setPassword(password);
        // return dataSource;

        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
