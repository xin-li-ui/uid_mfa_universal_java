package com.uidsecurity.utils;

import com.uidsecurity.constant.Constant;
import com.uidsecurity.exception.UidException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {

    public static String getAndValidateUrl(String host, String file) {
        try {
            URL url = new URL(Constant.HTTPS, host, file);
            return url.toString();
        } catch (MalformedURLException e) {
            throw new UidException(e.getMessage(), e);
        }
    }

    public static String buildQueryUrl(String httpUrl, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return httpUrl;
        }
        String query = queryParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return httpUrl + "?" + query;
    }
}
