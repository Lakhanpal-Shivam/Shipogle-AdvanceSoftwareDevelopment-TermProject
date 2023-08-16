package com.shipogle.app.service;

import com.shipogle.app.model.ForgotPasswordToken;
import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.ForgotPasswordTokenRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordTokenServiceTest {
    @InjectMocks
    ForgotPasswordTokenServiceImpl forgotPasswordTokenService;
    @Mock
    ForgotPasswordTokenRepository forgotPasswordTokenRepo;
    @Mock
    User user;
    @Test
    public void createForgotPasswordToken(){
        User user = new User();
        user.setId(1);
        user.setEmail("kadivarnand007@gmail.com");
        user.setFirst_name("Nand");
        user.setLast_name("Kadivar");
        user.setPassword("abc123");
        ForgotPasswordToken createdToken = forgotPasswordTokenService.createForgotPasswordToken(user);
        assertEquals(user,createdToken.getUser());
    }
}
