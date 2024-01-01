package com.hendisantika.multitenancy.config.db;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
@Component
@RequiredArgsConstructor
public class DatabaseMigration implements ApplicationListener<ContextRefreshedEvent> {

    private final DbConfigProperty dbConfigProperty;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        dbConfigProperty
                .getConfigurations()
                .forEach(
                        (key, value) -> {
                            Flyway flyway = Flyway.configure().dataSource(value.createDataSource()).load();
                            flyway.migrate();
                        });
    }
}
