package com.uidsecurity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "uid")
public record UidProperties(
        String clientId,
        String clientSecret,
        String apiHost,
        String redirectUrl
) {
}
