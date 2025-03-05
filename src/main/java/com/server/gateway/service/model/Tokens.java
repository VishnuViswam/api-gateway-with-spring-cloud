package com.server.gateway.service.model;

public class Tokens {
    private String token;
    private String refreshToken;

    public Tokens(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Tokens() {
        super();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
