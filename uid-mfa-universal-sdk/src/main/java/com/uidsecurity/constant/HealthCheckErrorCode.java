package com.uidsecurity.constant;

import lombok.Getter;

@Getter
public enum HealthCheckErrorCode {

    CLIENT_NOT_EXIST("40301", "The client not exist"),
    CLIENT_HAS_DISABLED("40302", "The client has been disabled"),
    INVALID_JWT("40303", "Invalid JWT signature"),
    JWT_PARAM_EMAIL_INVALID("40304", "The email cannot be empty in the jwt payload"),
    JWT_PARAM_ACTION_INVALID("40305", "The action cannot be empty in the jwt payload"),
    ACCESS_IP_IS_DENIED("40306", "The access ip address was denied"),
    USER_NOT_EXIST("40307", "The user does not exist"),
    ACTION_NOT_EXIST("40308", "This app mfa action does not defined in UID service"),
    POLICY_NOT_MATCHED("40309", "The policy and rule are not matched"),
    RULE_USER_ACCESS_IS_BLOCKED("40310", "The rule access is blocked"),
    RULE_MFA_IS_NOT_REQUIRED("40311", "The rule mfa auth type is not required"),
    ;

    private final String code;
    private final String msg;

    HealthCheckErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
