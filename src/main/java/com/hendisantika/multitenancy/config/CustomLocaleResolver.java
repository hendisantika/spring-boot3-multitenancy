package com.hendisantika.multitenancy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot3-multitenancy
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/1/24
 * Time: 07:48
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class CustomLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("id"));
}
