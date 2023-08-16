package com.shipogle.app.controller;

import com.shipogle.app.service.RatingServiceImpl;
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
public class RatingControllerTests {
    @InjectMocks
    RatingController ratingController;
    @Mock
    RatingServiceImpl ratingService;

    private final float TEST_START_RATING = 4.5f;
    @Test
    public void postRatingTest() {
        Map<String, String> req = new HashMap<>();
        req.put("driver_route_id","1");
        req.put("star","4.5");
        req.put("review","review");
        ratingController.postRating(req);

        verify(ratingService,times(1)).storeRating(1L,TEST_START_RATING,"review");
    }

    @Test
    public void getDelivererRatingTest() {
        ratingController.getDelivererRating();

        verify(ratingService,times(1)).getDelivererRating();
    }

    @Test
    public void getPostedTest() {
        ratingController.getSenderPostedRating();

        verify(ratingService,times(1)).getSenderPostedRating();
    }

    @Test
    public void deletePackageRequestTest() {
        Map<String, String> req = new HashMap<>();
        req.put("rating_id","1");
        ratingController.deleteRating(req);

        verify(ratingService,times(1)).deleteRating(1);
    }

    @Test
    public void getDelivererRatingByIDTest() {
        ratingController.getDelivererRatingById(1);

        verify(ratingService,times(1)).getDelivererRatingWithID(1);
    }
}
