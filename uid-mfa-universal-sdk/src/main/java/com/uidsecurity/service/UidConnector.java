package com.uidsecurity.service;

import com.uidsecurity.constant.Constant;
import com.uidsecurity.exception.UidException;
import com.uidsecurity.model.HealthCheckResponse;
import com.uidsecurity.model.ResponseMessage;
import com.uidsecurity.model.TokenResponse;
import com.uidsecurity.utils.JSONUtil;
import com.uidsecurity.utils.OkHttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UidConnector {

    private final String baseUrl;

    private final OkHttpClientUtil okHttpClientUtil;

    public UidConnector(String baseUrl) {
        this.baseUrl = baseUrl;
        okHttpClientUtil = OkHttpClientUtil.getInstance();
    }

    public HealthCheckResponse healthCheck(String clientId, String clientAssertion) {
        String url = baseUrl + Constant.OAUTH_V1_HEALTH_CHECK_ENDPOINT;
        Map<String, String> params = new HashMap<>(2);
        params.put("client_id", clientId);
        params.put("client_assertion", clientAssertion);
        try {
            String jsonResponse = okHttpClientUtil.post(url, params);
            ResponseMessage objectResponse = JSONUtil.toObject(jsonResponse, ResponseMessage.class);
            return JSONUtil.toObject(JSONUtil.toJSON(objectResponse.getData()), HealthCheckResponse.class);
        } catch (IOException e) {
            throw new UidException(e.getMessage(), e);
        }
    }

    public TokenResponse getToken(String grantType,
                                  String code,
                                  String clientId,
                                  String redirectUri,
                                  String clientAssertionType,
                                  String clientAssertion) {
        String url = baseUrl + Constant.OAUTH_V1_TOKEN_ENDPOINT;
        Map<String, String> params = new HashMap<>(6);
        params.put("grant_type", grantType);
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("client_assertion_type", clientAssertionType);
        params.put("client_assertion", clientAssertion);
        try {
            String jsonResponse = okHttpClientUtil.post(url, params);
            ResponseMessage objectResponse = JSONUtil.toObject(jsonResponse, ResponseMessage.class);
            return JSONUtil.toObject(JSONUtil.toJSON(objectResponse.getData()), TokenResponse.class);
        } catch (IOException e) {
            throw new UidException(e.getMessage(), e);
        }
    }

}
