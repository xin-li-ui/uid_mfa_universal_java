package com.uidsecurity.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.uidsecurity.UidClient;
import com.uidsecurity.exception.UidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {

    public static final String CLAIM_NAME_IP = "ip";
    public static final String CLAIM_NAME_UA = "ua";
    public static final String CLAIM_NAME_EMAIL = "email";
    public static final String CLAIM_NAME_ACTION = "action";
    public static final String CLAIM_NAME_CLIENT_ID = "client_id";
    public static final String CLAIM_NAME_STATE = "state";
    public static final String CLAIM_NAME_REDIRECT_URI = "redirect_uri";
    public static final String CLAIM_NAME_SCOPE = "scope";
    public static final String CLAIM_NAME_RESPONSE_TYPE = "response_type";

    public static String generateHealthCheckClientAssertion(UidClient uidClient, String aud, String ip, String userAgent, String email, String action) {

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

        Instant instant = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(Date.from(instant))
                .expirationTime(Date.from(instant.plus(10, ChronoUnit.MINUTES)))
                .issuer(uidClient.getClientId())
                .subject(uidClient.getClientId())
                .audience(aud)
                .claim(CLAIM_NAME_IP, ip)
                .claim(CLAIM_NAME_UA, userAgent)
                .claim(CLAIM_NAME_EMAIL, email)
                .claim(CLAIM_NAME_ACTION, action)
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            JWSSigner signer = new MACSigner(uidClient.getClientSecret());
            jwt.sign(signer);
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new UidException(e.getMessage(), e);
        }
    }

    public static String generateRequestStr(UidClient uidClient, String email, String action, String state, String ip, String ua) {

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).build();

        Instant instant = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(Date.from(instant))
                .expirationTime(Date.from(instant.plus(10, ChronoUnit.MINUTES)))
                .claim(CLAIM_NAME_SCOPE, "openid")
                .claim(CLAIM_NAME_RESPONSE_TYPE, "code")
                .claim(CLAIM_NAME_CLIENT_ID, uidClient.getClientId())
                .claim(CLAIM_NAME_REDIRECT_URI, uidClient.getRedirectUri())
                .claim(CLAIM_NAME_STATE, state)
                .claim(CLAIM_NAME_EMAIL, email)
                .claim(CLAIM_NAME_ACTION, action)
                .claim(CLAIM_NAME_IP, ip)
                .claim(CLAIM_NAME_UA, ua)
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            JWSSigner signer = new MACSigner(uidClient.getClientSecret());
            jwt.sign(signer);
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new UidException(e.getMessage(), e);
        }

    }

    public static String generateTokenClientAssertion(UidClient uidClient, String aud) {

        Instant instant = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(Date.from(instant))
                .expirationTime(Date.from(instant.plus(10, ChronoUnit.MINUTES)))
                .issuer(uidClient.getClientId())
                .subject(uidClient.getClientId())
                .audience(aud)
                .build();

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            JWSSigner signer = new MACSigner(uidClient.getClientSecret());
            jwt.sign(signer);
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new UidException(e.getMessage(), e);
        }
    }

    public static IDTokenClaimsSet validateAndDecodeIdToken(UidClient uidClient, String jwt) {

        try {
            JWSVerifier verifier = new MACVerifier(uidClient.getClientSecret());
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            if (!signedJWT.verify(verifier)) {
                throw new JOSEException("Invalid JWT signature");
            }
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return new IDTokenClaimsSet(claimsSet);
        } catch (Exception e) {
            throw new UidException(e.getMessage(), e);
        }
    }

}