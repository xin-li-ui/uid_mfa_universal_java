package com.uidsecurity.utils;

import com.uidsecurity.exception.UidException;

import java.net.MalformedURLException;
import java.net.URL;

public class Validator {

    private static final int CLIENT_ID_LENGTH = 20;
    private static final String CLIENT_ID_REGEX = "^[A-Z0-9]+$";

    private static final int CLIENT_SECRET_LENGTH = 40;
    private static final String CLIENT_SECRET_REGEX = "^[A-Za-z0-9]+$";

    public static void validateClientParams(String clientId, String clientSecret, String apiHost, String redirectUri) {
        if (clientId.length() != CLIENT_ID_LENGTH || !clientId.matches(CLIENT_ID_REGEX)) {
            throw new UidException("Invalid client id");
        }
        if (clientSecret.length() != CLIENT_SECRET_LENGTH || !clientSecret.matches(CLIENT_SECRET_REGEX)) {
            throw new UidException("Invalid client secret");
        }
        HttpUtil.getAndValidateUrl(apiHost, "");
        try {
            new URL(redirectUri);
        } catch (MalformedURLException e) {
            throw new UidException("Invalid redirect_uri");
        }
    }
}
