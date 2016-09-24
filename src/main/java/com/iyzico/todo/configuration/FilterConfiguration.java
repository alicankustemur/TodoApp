package com.iyzico.todo.configuration;

import com.iyzico.todo.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by heval on 22.09.2016.
 */
@Configuration
public class FilterConfiguration {

    private static final String[] FORBIDDEN_URL_WITHOUT_SESSION = {"/todo", "/search"};

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(loginFilter());
        registration.addUrlPatterns(FORBIDDEN_URL_WITHOUT_SESSION);
        registration.setName("loginFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "loginFilter")
    public Filter loginFilter() {
        return new LoginFilter();
    }
}
