package com.uidsecurity.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -3138823399834806194L;

    private String idToken;

    private String accessToken;

    private String refreshToken;

    /**
     * eg: Bearer
     */
    private String tokenType;

    /**
     * e.g: 299
     */
    private Integer expiresIn;

    /**
     * e.g: openid
     */
    private String scope;
}