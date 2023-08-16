package com.shipogle.app.controller;

import com.shipogle.app.model.User;
import com.shipogle.app.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthControllerTests {
    @InjectMocks
    AuthController authController;
    @Mock
    AuthServiceImpl authService;
    @Mock
    HttpServletRequest request;
    @Mock
    User user;

    @Test
    public void loginTest() {
        Map<String, String> req = new HashMap<>();
        req.put("email","kadivarnand007@gmail.com");
        req.put("password","abc123");

        authController.login(req);

        verify(authService,times(1)).login("kadivarnand007@gmail.com","abc123");
    }

    @Test
    public void registerTest() {
        User user = new User();
        user.setFirst_name("Nand");
        user.setLast_name("Kadivar");
        user.setEmail("kadivarnand007@gmailc.om");
        user.setPassword("password123");

        authController.registerNewUser(user);

        verify(authService,times(1)).register(user);
    }

    @Test
    public void changePasswordTest() {
        Map<String, String> req = new HashMap<>();
        req.put("token","forgot password token");
        req.put("password","abc123");

        authController.changePassword(req);

        verify(authService,times(1)).resetPassword("forgot password token","abc123");
    }

    @Test
    public void forgotPasswordTest() {
        Map<String, String> req = new HashMap<>();
        req.put("email","kadivarnand007@gmail.com");
        when(request.getHeader(any())).thenReturn("");
        authController.forgotPassword(request,req);

        verify(authService,times(1)).forgotPassword("kadivarnand007@gmail.com");
    }

    @Test
    public void emailVerificationTest() {
        authController.emailVerification("code",1);

        verify(authService,times(1)).verifyEmail("code",1);
    }

    @Test
    public void getUserInfoTest() {
        authController.getUserInfo("token");
        verify(authService,times(1)).getUserInfo("token");
    }

    @Test
    public void updateUserTest() {
        authController.updateUser("token",user);
        verify(authService,times(1)).updateUser("token",user);
    }
}

