package com.shipogle.app.service;

import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;

import java.security.Key;

public interface JwtTokenService {
    public JwtToken createJwtToken(User user);

    public Key generateKey();

    public void deactiveUserTokens(User user);

    public boolean isJwtActive(String token);
}
