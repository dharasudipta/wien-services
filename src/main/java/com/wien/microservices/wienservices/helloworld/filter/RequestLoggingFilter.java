package com.wien.microservices.wienservices.helloworld.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilter {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        commonsRequestLoggingFilter.setIncludeQueryString(true);
        commonsRequestLoggingFilter.setIncludePayload(true);
        commonsRequestLoggingFilter.setMaxPayloadLength(10000);
        commonsRequestLoggingFilter.setIncludeHeaders(true);
        commonsRequestLoggingFilter.setAfterMessagePrefix("[DEBUG]REQUEST DATA: ");
        return commonsRequestLoggingFilter;
    }
}
