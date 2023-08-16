package com.shipogle.app.service;

import com.shipogle.app.model.User;


public interface AuthService {
    public boolean isAlreadyExist(User user);

    public String resetPassword(String token, String password);

    public String forgotPassword(String email);
    public String verifyEmail(String code, int id);

    public String register(User new_user);

    public String login(String email, String password);

    public User getUser(int id);

    public User getUserInfo(String token);
    public String updateUser(String token, User user);
}
