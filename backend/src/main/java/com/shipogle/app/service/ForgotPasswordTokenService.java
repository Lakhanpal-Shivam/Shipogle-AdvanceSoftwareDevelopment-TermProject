package com.shipogle.app.service;

import com.shipogle.app.model.User;

import java.security.Key;

public interface ForgotPasswordTokenService {
    public com.shipogle.app.model.ForgotPasswordToken createForgotPasswordToken(User user);

    public Key generateKey();
}
