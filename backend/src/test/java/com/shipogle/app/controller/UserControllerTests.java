package com.shipogle.app.controller;

import com.shipogle.app.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
    @InjectMocks
    UserController userController;
    @Mock
    UserServiceImpl userService;

    @Test
    public void updateLocationTest() {
        Map<String, String> req = new HashMap<>();
        req.put("latitude","113151");
        req.put("longitude","016554");
        userController.updateUserLocation(req);

        verify(userService,times(1)).updateUserLocation("113151","016554");
    }

    @Test
    public void getLocationTest() {
        Map<String, String> req = new HashMap<>();
        req.put("user_id","1");
        userController.getUserLocation(req);

        verify(userService,times(1)).getUserLocation(1);
    }
}
