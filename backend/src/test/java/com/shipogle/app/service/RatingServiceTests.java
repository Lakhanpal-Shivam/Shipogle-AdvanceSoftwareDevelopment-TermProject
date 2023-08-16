package com.shipogle.app.service;

import com.shipogle.app.model.DriverRoute;
import com.shipogle.app.model.Rating;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.DriverRouteRepository;
import com.shipogle.app.repository.RatingRepository;
import com.shipogle.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTests {
    @InjectMocks
    RatingServiceImpl ratingService;
    @Mock
    DriverRouteRepository driverRouteRepo;
    @Mock
    DriverRoute driverRoute;
    @Mock
    RatingRepository ratingRepo;
    @Mock
    UserService userService;
    @Mock
    UserRepository userRepo;
    @Mock
    User user;
    @Mock
    Rating rating;
    private final float TEST_RATING = 4.5f;

    @Test
    public void storeRatingTestInvalidDriverRoute(){
        when(driverRouteRepo.getDriverRouteById(1L)).thenReturn(null);
        when(userService.getLoggedInUser()).thenReturn(user);

        assertEquals("Not able to post rating",ratingService.storeRating(1L,TEST_RATING,"review"));
    }

    @Test
    public void storeRatingTestNullSender(){
        when(driverRouteRepo.getDriverRouteById(1L)).thenReturn(driverRoute);
        when(userService.getLoggedInUser()).thenReturn(null);

        assertEquals("Not able to post rating",ratingService.storeRating(1L,TEST_RATING,"review"));
    }

    @Test
    public void storeRatingTestSuccess(){

        when(driverRouteRepo.getDriverRouteById(1L)).thenReturn(driverRoute);
        when(userService.getLoggedInUser()).thenReturn(user);

        assertEquals("Rating is posted",ratingService.storeRating(1L,TEST_RATING,"review"));
    }

    @Test
    public void storeRatingTestUserNotFoundException(){

        when(driverRouteRepo.getDriverRouteById(1L)).thenReturn(driverRoute);
        when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);

        assertEquals(null,ratingService.storeRating(1L,TEST_RATING,"review"));
    }

    @Test
    public void deleteRatingTestException(){
        when(ratingRepo.getRatingById(1)).thenThrow(IllegalArgumentException.class);
        assertEquals(null,ratingService.deleteRating(1));
    }

    @Test
    public void deleteRatingTestSuccess(){
        when(ratingRepo.getRatingById(1)).thenReturn(rating);
        assertEquals("Rating deleted",ratingService.deleteRating(1));
    }

    @Test
    public void getDelivererRatingTestUserNotFoundException(){
        when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);
        assertEquals(null,ratingService.getDelivererRating());
    }

    @Test
    public void getDelivererRatingTestSuccess(){
        List<Rating> ratings = new ArrayList<>();
        when(userService.getLoggedInUser()).thenReturn(user);
        when(user.getUser_id()).thenReturn(1);
        when(ratingRepo.getAllByDriverRoute_DriverId("1")).thenReturn(ratings);
        assertEquals(ratings,ratingService.getDelivererRating());
    }

    @Test
    public void getSenderPostedRatingTestUserNotFoundException(){
        when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);
        assertEquals(null,ratingService.getSenderPostedRating());
    }

    @Test
    public void getSenderPostedRatingTestSuccess(){
        List<Rating> ratings = new ArrayList<>();
        when(userService.getLoggedInUser()).thenReturn(user);
        when(user.getUser_id()).thenReturn(1);
        when(ratingRepo.getAllByUser_Id(1)).thenReturn(ratings);
        assertEquals(ratings,ratingService.getSenderPostedRating());
    }

    @Test
    public void getDelivererRatingByIDTestUserNotFoundException(){
        when(userRepo.getUserById(Integer.valueOf(1))).thenThrow(UsernameNotFoundException.class);
        assertEquals(null,ratingService.getDelivererRatingWithID(Integer.valueOf(1)));
    }

    @Test
    public void getDelivererRatingByIDTestSuccess(){
        List<Rating> ratings = new ArrayList<>();
        when(userRepo.getUserById(Integer.valueOf(1))).thenReturn(user);
        when(user.getUser_id()).thenReturn(1);
        when(ratingRepo.getAllByDriverRoute_DriverId("1")).thenReturn(ratings);
        assertEquals(ratings,ratingService.getDelivererRatingWithID(Integer.valueOf(1)));
    }
}
