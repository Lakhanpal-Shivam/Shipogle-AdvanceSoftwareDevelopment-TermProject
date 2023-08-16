package com.shipogle.app.service;

import com.shipogle.app.model.ForgotPasswordToken;
import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.ForgotPasswordTokenRepository;
import com.shipogle.app.repository.JwtTokenRepository;
import com.shipogle.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    UserRepository userRepo;

    @Mock
    User user;

    @Mock
    ForgotPasswordToken forgotPasswordToken;

    @Mock
    MailServiceImpl mailService;

    @Mock
    ForgotPasswordTokenService forgotPasswordTokenService;

    @Mock
    ForgotPasswordTokenRepository forgotPasswordTokenRepo;

    @Mock
    Claims claims;

    @Mock
    BCryptPasswordEncoder encoder;

    @Mock
    AuthenticationManager authManager;

    @Mock
    UsernamePasswordAuthenticationToken authToken;

    @Mock
    JwtTokenService jwtTokenService;

    @Mock
    JwtTokenRepository jwtTokenRepo;

    @Mock
    JwtToken token;
    private final int TEST_USER_ID = 40;
    private final String TEST_TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJlbWFpbCI6ImthZGl2YXJuYW5kMDA3QGdtYWlsLmNvbSIsInN1YiI6Ik5hbmQiLCJpYXQiOjE2ODA4ODE1NTUsImV4cCI6MTgzNjQwMTU1NX0.EjKLqlrUT2zhuTjh_it8-APBTObClIZVpPbxCKtbw1wUB3iseec1jChxlLco8TU2";

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void isAlreadyExistTestPositive() {
        
        Mockito.when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");

        Mockito.when(userRepo.findUserByEmail(user.getEmail())).thenReturn(user);

        assertTrue(authService.isAlreadyExist(user));
    }

    @Test
    public void isAlreadyExistTestNegative() {
        Mockito.when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.lenient().when(userRepo.findUserByEmail(user.getEmail())).thenReturn(null);

        assertFalse(authService.isAlreadyExist(user));
    }

    @Test
    public void verifyEmailTestUserAlreadyVerified() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String code = encoder.encode("kadivarnand007@gmail.com");

        Mockito.lenient().when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.lenient().when(user.getIs_verified()).thenReturn(true);
        Mockito.lenient().when(user.getUser_id()).thenReturn(TEST_USER_ID);
        Mockito.lenient().when(userRepo.getById(user.getUser_id())).thenReturn(user);

        assertThrows(ResponseStatusException.class,() -> authService.verifyEmail(code, user.getUser_id()));
    }

    @Test
    public void verifyEmailTestVerifyUser() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String code = encoder.encode("kadivarnand007@gmail.com");

        Mockito.lenient().when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.lenient().when(user.getIs_verified()).thenReturn(false);
        Mockito.lenient().when(user.getUser_id()).thenReturn(TEST_USER_ID);
        Mockito.lenient().when(userRepo.getById(user.getUser_id())).thenReturn(user);

        assertEquals("Email Verified",authService.verifyEmail(code, user.getUser_id()));
    }

    @Test
    public void verifyEmailTestVerifyNotValidUser() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String code = encoder.encode("kadivarnand007@gmail.com");

        Mockito.lenient().when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.lenient().when(user.getIs_verified()).thenReturn(false);
        Mockito.lenient().when(user.getUser_id()).thenReturn(TEST_USER_ID);
        Mockito.lenient().when(userRepo.getById(user.getUser_id())).thenReturn(null);

        assertThrows(ResponseStatusException.class,() -> authService.verifyEmail(code, user.getUser_id()));
    }

    @Test
    public void forgotPasswordTestEmailSend() {

        Mockito.when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.when(userRepo.getUserByEmail("kadivarnand007@gmail.com")).thenReturn(user);

        Mockito.when(forgotPasswordTokenService.createForgotPasswordToken(user)).thenReturn(forgotPasswordToken);
        Mockito.when(forgotPasswordToken.getForgot_password_token()).thenReturn("token");

        assertEquals("Password reset link sent", authService.forgotPassword("kadivarnand007@gmail.com"));
    }

    @Test
    public void forgotPasswordTestEmail() {

        Mockito.when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.when(userRepo.getUserByEmail("kadivarnand007@gmail.com")).thenReturn(null);

        Mockito.when(forgotPasswordTokenService.createForgotPasswordToken(user)).thenReturn(forgotPasswordToken);
        Mockito.when(forgotPasswordToken.getForgot_password_token()).thenReturn("token");

        assertThrows(ResponseStatusException.class,() -> authService.forgotPassword("kadivarnand007@gmail.com"));
    }

    @Test
    public void resetPasswordTestExpiredToken(){
        when(forgotPasswordTokenRepo.findByForgetPasswordToken("token")).thenReturn(forgotPasswordToken);
        when(forgotPasswordToken.getIs_active()).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->authService.resetPassword("token","abc123"));
    }

    @Test
    public void resetPasswordTest(){
        String token = TEST_TOKEN;
        String password = "abc213";
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setIs_active(true);
        User user = new User();
        user.setEmail("kadivarnand007@gmail.com");
        when(forgotPasswordTokenRepo.findByForgetPasswordToken(token)).thenReturn(forgotPasswordToken);
        Mockito.lenient().when(encoder.encode(password)).thenReturn("encodedPassword");
        when(userRepo.getUserByEmail(any())).thenReturn(user);

        // Act
        String result = authService.resetPassword(token, password);

        // Assert
        assertEquals("Password changed successfully", result);
        assertEquals(false, forgotPasswordToken.getIs_active());
    }

    @Test
    public void registerTestUserAlreadyExist() {
        authService = Mockito.spy(new AuthServiceImpl());
        Mockito.doReturn(true).when(authService).isAlreadyExist(user);

        assertThrows(ResponseStatusException.class,() -> authService.register(user));
    }

    @Test
    public void registerTestUserRegistration() {
        User user = new User();
        user.setEmail("kadivarnand007@gmail.com");
        user.setPassword("abc123");
        user.setIs_verified(false);
        Mockito.when(userRepo.findUserByEmail(user.getEmail())).thenReturn(null);
        assertEquals("Verification email sent", authService.register(user));
    }

    @Test
    public void loginTestNotVerifiedUser(){
        Mockito.when(user.getEmail()).thenReturn("kadivarnand007@gmail.com");
        Mockito.when(user.getIs_verified()).thenReturn(false);
        Mockito.when(userRepo.getUserByEmail(user.getEmail())).thenReturn(user);
        assertThrows(ResponseStatusException.class,()->authService.login("kadivarnand007@gmail.com","abc123"));
    }

    @Test
    public void loginTestVerifiedUser(){
        User user = new User();
        user.setEmail("kadivarnand007@gmail.com");
        user.setIs_verified(true);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "abc123");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.lenient().when(userRepo.getUserByEmail(user.getEmail())).thenReturn(user);

        Mockito.lenient().when(jwtTokenService.createJwtToken(user)).thenReturn(token);

        authService.login("kadivarnand007@gmail.com","abc123");
        Mockito.verify(jwtTokenService,times(1)).deactiveUserTokens(user);
    }
}
