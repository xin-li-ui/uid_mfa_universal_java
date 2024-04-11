package com.uidsecurity;

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.uidsecurity.constant.Constant;
import com.uidsecurity.exception.UidException;
import com.uidsecurity.model.HealthCheckResponse;
import com.uidsecurity.model.TokenResponse;
import com.uidsecurity.service.UidConnector;
import com.uidsecurity.utils.HttpUtil;
import com.uidsecurity.utils.JwtUtil;
import com.uidsecurity.utils.Validator;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UidClient {

    @Getter
    private String clientId;
    @Getter
    private String clientSecret;
    private String apiHost;
    @Getter
    private String redirectUri;

    private UidConnector uidConnector;

    private UidClient() {
    }

    private boolean isSecondDomain() {
        return !apiHost.contains("api-gw");
    }

    private String getFile(String file) {
        String gwPrefix = isSecondDomain() ? "/gw" : "";
        return gwPrefix + file;
    }

    private String getPortalFile(String file) {
        String portalPrefix = isSecondDomain() ? "/portal" : "";
        return portalPrefix + file;
    }

    private String getPortalHost() {
        if (isSecondDomain()) {
            return apiHost;
        }
        return apiHost.replace("api-gw", "portal");
    }

    public static class Builder {
        private final String clientId;
        private final String clientSecret;
        private final String apiHost;

        private final String redirectUri;

        public Builder(String clientId, String clientSecret, String apiHost, String redirectUri) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.apiHost = apiHost;
            this.redirectUri = redirectUri;
        }

        public UidClient build() {
            Validator.validateClientParams(clientId, clientSecret, apiHost, redirectUri);

            UidClient client = new UidClient();
            client.clientId = clientId;
            client.clientSecret = clientSecret;
            client.apiHost = apiHost;
            client.redirectUri = redirectUri;

            String file = client.isSecondDomain() ? "/gw" : "";
            client.uidConnector = new UidConnector(HttpUtil.getAndValidateUrl(apiHost, file));
            return client;
        }
    }

    public HealthCheckResponse healthCheck(String email, String action, String ip, String ua) {
        String aud = HttpUtil.getAndValidateUrl(apiHost, getFile(Constant.OAUTH_V1_HEALTH_CHECK_ENDPOINT));

        String clientAssertion = JwtUtil.generateHealthCheckClientAssertion(this, aud, ip, ua, email, action);
        HealthCheckResponse response = uidConnector.healthCheck(clientId, clientAssertion);
        if (!response.wasSuccess()) {
            throw new UidException(response.getMessage());
        }
        return response;
    }

    public String createAuthUrl(String email, String action, String state, String ip, String ua) {

        String request = JwtUtil.generateRequestStr(this, email, action, state, ip, ua);

        Map<String, String> queryParams = new HashMap<>(6);
        queryParams.put("scope", "openid");
        queryParams.put("response_type", "code");
        queryParams.put("state", state);
        queryParams.put("client_id", clientId);
        queryParams.put("redirect_uri", redirectUri);
        queryParams.put("request", request);

        String authUrl = HttpUtil.getAndValidateUrl(getPortalHost(), getPortalFile(Constant.OAUTH_AUTHORIZE_ENDPOINT));

        return HttpUtil.buildQueryUrl(authUrl, queryParams);
    }

    public IDTokenClaimsSet getIdToken(String code) {

        String aud = HttpUtil.getAndValidateUrl(apiHost, getFile(Constant.OAUTH_V1_TOKEN_ENDPOINT));
        String jwt = JwtUtil.generateTokenClientAssertion(this, aud);

        TokenResponse tokenResponse = uidConnector.getToken("authorization_code", code, clientId, redirectUri, JWTAuthentication.CLIENT_ASSERTION_TYPE, jwt);
        if (Objects.isNull(tokenResponse)) {
            throw new UidException("tokenResponse is null");
        }
        if (StringUtils.isBlank(tokenResponse.getIdToken())) {
            throw new UidException("IDToken is null");
        }
        String idTokenJwt = tokenResponse.getIdToken();
        return JwtUtil.validateAndDecodeIdToken(this, idTokenJwt);
    }

    public String generateState() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}