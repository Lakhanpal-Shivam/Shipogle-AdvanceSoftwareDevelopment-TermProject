package com.shipogle.app.service;

import com.shipogle.app.model.User;
import com.shipogle.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepo;

    @Mock
    User user;
    private final int TEST_USER_ID = 40;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getLoggedInUserTest(){
        String userEmail = "kadivarnand007@gmail.com";
        User user = new User();
        user.setEmail(userEmail);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, "abc123");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepo.getUserByEmail(userEmail)).thenReturn(user);

        User result = userService.getLoggedInUser();

        assertEquals(user, result);
    }

    @Test
    public void updateUserLocationTest(){
        userService = Mockito.spy(UserServiceImpl.class);

        userService.userRepo = this.userRepo;

        User user1 = new User();
        user1.setFirst_name("Nand");
        user1.setEmail("kadivarnand007@gmail.com");

        String latitude = "40.7128";
        String longitude = "-74.0060";

        Mockito.doReturn(user1).when(userService).getLoggedInUser();
        Mockito.when(userRepo.save(any())).thenReturn(user1);
        String result = userService.updateUserLocation(latitude, longitude);

        assertEquals("Location updated", result);
    }

    @Test
    public void getUserLocationTest(){
        Mockito.when(userRepo.getUserById(TEST_USER_ID)).thenReturn(user);
        Mockito.when(user.getLatitude()).thenReturn("44.8857° N");
        Mockito.when(user.getLongitude()).thenReturn("63.1005° W");

        Map<String,String> coordinates = new HashMap<>();
        coordinates.put("latitude","44.8857° N");
        coordinates.put("longitude", "63.1005° W");

        assertEquals(coordinates,userService.getUserLocation(TEST_USER_ID));
    }

    @Test
    public void getUserLocationTestNullUser(){
        Mockito.when(userRepo.getUserById(TEST_USER_ID)).thenReturn(null);

        assertEquals(null,userService.getUserLocation(TEST_USER_ID));
    }
}
