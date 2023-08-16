package com.shipogle.app.service;

import com.shipogle.app.model.JwtToken;
import com.shipogle.app.repository.JwtTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ExtendWith(MockitoExtension.class)
public class LogoutHandlerServiceTest {
    @InjectMocks
    LogoutHandlerServiceImpl logoutHandlerService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    Authentication authentication;
    @Mock
    JwtTokenRepository jwtTokenRepo;
    @Mock
    JwtToken token;


    @Test
    public void logoutTestSuccess(){
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer token");
        Mockito.when(jwtTokenRepo.getJwtTokensByToken("token")).thenReturn(token);
        logoutHandlerService.logout(request,response,authentication);
        Mockito.verify(jwtTokenRepo,Mockito.times(1)).save(token);
    }

    @Test
    public void logoutTestNotBearerToken(){
        Mockito.when(request.getHeader("Authorization")).thenReturn("NonBearer token");
        logoutHandlerService.logout(request,response,authentication);
        Mockito.verify(jwtTokenRepo,Mockito.times(0)).save(token);
    }
}
