package com.dundermifflin.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
public class PaginationConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePageable() {
        return p -> {
            p.setMaxPageSize(100);
            p.setOneIndexedParameters(true);
            p.setFallbackPageable(org.springframework.data.domain.PageRequest.of(0, 20));
        };
    }
}
