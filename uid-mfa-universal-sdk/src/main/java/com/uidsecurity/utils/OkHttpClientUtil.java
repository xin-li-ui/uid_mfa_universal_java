package com.uidsecurity.utils;

import okhttp3.*;
import java.io.IOException;
import java.util.Map;

public class OkHttpClientUtil {

    private OkHttpClient client;

    private OkHttpClientUtil() {
        init();
    }

    private void init() {
        client = new OkHttpClient();
    }

    private static final class OkHttpClientUtilHolder {
        private static final OkHttpClientUtil instance = new OkHttpClientUtil();
    }

    public static OkHttpClientUtil getInstance() {
        return OkHttpClientUtilHolder.instance;
    }

    /**
     * execute request return string
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String execute(Request request) throws IOException {
        Response res = client.newCall(request).execute();
        if (res.isSuccessful()) {
            return res.body().string();
        }
        throw new RuntimeException("http request error: " + request.url().toString() + "," + res.body().string());
    }

    /**
     * send http request synchronously
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public String post(String url, Map<String, String> param) throws IOException {
        Request request = buildPostRequestWithHeaders(url, param);
        return execute(request);
    }

    private Request buildPostRequestWithHeaders(String url, Map<String, String> param) {
        FormBody.Builder builder = new FormBody.Builder();
        setParam(param, builder);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        return requestBuilder.post(builder.build()).build();
    }


    private void setParam(Map<String, String> param, FormBody.Builder builder) {
        if (param != null && !param.isEmpty()) {
            param.forEach(builder::add);
        }
    }


}