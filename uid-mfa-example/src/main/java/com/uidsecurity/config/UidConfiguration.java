package com.uidsecurity.config;

import com.uidsecurity.UidClient;
import com.uidsecurity.exception.UidException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UidConfiguration {

    @Bean
    public UidClient uidClient(UidProperties uidProperties) throws UidException {
        return new UidClient.Builder(
                uidProperties.clientId(),
                uidProperties.clientSecret(),
                uidProperties.apiHost(),
                uidProperties.redirectUrl()
        ).build();
    }
}
