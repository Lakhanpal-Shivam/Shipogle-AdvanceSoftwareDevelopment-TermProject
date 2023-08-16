package com.shipogle.app.service;

import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.JwtTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTests {
    @InjectMocks
    JwtTokenServiceImpl jwtTokenService;
    @Mock
    JwtTokenRepository jwtTokenRepo;
    @Mock
    JwtToken token;

    @Mock
    User user;

    @Test
    public void createJwtTokenTest(){
        User user = new User();
        user.setId(1);
        user.setEmail("kadivarnand007@gmail.com");
        user.setFirst_name("Nand");
        user.setLast_name("Kadivar");
        user.setPassword("abc123");
        JwtToken createdToken = jwtTokenService.createJwtToken(user);
        assertEquals(user,createdToken.getUser());
    }

    @Test
    public void deactiveUserTestSuccess(){
        List<JwtToken> tokens = new ArrayList<>();
        tokens.add(token);
        when(jwtTokenRepo.getAllByUser(user)).thenReturn(tokens);
        jwtTokenService.deactiveUserTokens(user);
        Mockito.verify(token,Mockito.times(1)).setIs_active(false);
    }

    @Test
    public void deactiveUserTestStoreTokens(){
        List<JwtToken> tokens = new ArrayList<>();
        tokens.add(token);
        when(jwtTokenRepo.getAllByUser(user)).thenReturn(tokens);
        jwtTokenService.deactiveUserTokens(user);
        Mockito.verify(jwtTokenRepo,Mockito.times(1)).saveAll(tokens);
    }

    @Test
    public void isJwtActiveTestPositive(){
        when(jwtTokenRepo.getJwtTokensByToken("jwt token")).thenReturn(token);
        when(token.getIs_active()).thenReturn(true);
        assertTrue(jwtTokenService.isJwtActive("jwt token"));
    }

    @Test
    public void isJwtActiveTestNegative(){
        when(jwtTokenRepo.getJwtTokensByToken("jwt token")).thenReturn(token);
        when(token.getIs_active()).thenReturn(false);
        assertFalse(jwtTokenService.isJwtActive("jwt token"));
    }

    @Test
    public void generateKeyTest() {
        Key key = jwtTokenService.generateKey();
        assertTrue(key.getAlgorithm().equals("HmacSHA256"));
    }

    @Test
    public void deactiveUserTokensTest(){
        User user1 = new User();
        user1.setEmail("kadivarnand007@gmail.com");
        List<JwtToken> tokens = new ArrayList<>();
        JwtToken token1 = new JwtToken();
        token1.setUser(user1);
        JwtToken token2 = new JwtToken();
        token2.setUser(user1);
        tokens.add(token1);
        tokens.add(token2);

        when(jwtTokenRepo.getAllByUser(user1)).thenReturn(tokens);
        jwtTokenService.deactiveUserTokens(user1);
        assertFalse(token1.getIs_active());
    }
}
