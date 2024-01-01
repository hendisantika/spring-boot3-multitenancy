package com.hendisantika.multitenancy.config.db;

import com.hendisantika.multitenancy.SpringBoot3MultitenancyApplication;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/2/24
 * Time: 04:58
 * To change this template use File | Settings | File Templates.
 */
public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String lang = SpringBoot3MultitenancyApplication.defaultProperties.getProperty("lang");
        System.out.println("lang = " + lang);
        if (Objects.nonNull(lang)) {
            return lang;
        } else {
            return "en";
        }
    }
}
