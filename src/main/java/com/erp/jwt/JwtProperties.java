package com.erp.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${jwt.secret}")
    private String SECRET;
    public static final int EXPIRATION_TIME = 1000 * 30 * 120;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public String getSecret() {
        return SECRET;
    }
}