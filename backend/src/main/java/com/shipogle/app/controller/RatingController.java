package com.shipogle.app.controller;

import com.shipogle.app.model.Rating;
import com.shipogle.app.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RatingController {
    @Autowired
    RatingService ratingService;

    /**
     * Post rating
     *
     * @author Shahraj Singh
     * @param req request
     * @return response
     */
    @PostMapping("rating/post")
    public String postRating(@RequestBody Map<String,String> req){
        Long driver_route_id = Long.valueOf(req.get("driver_route_id"));
        float start = Float.parseFloat(req.get("star"));
        String review = req.get("review");
        return ratingService.storeRating(driver_route_id,start,review);
    }

    /**
     * Get all driver ratings
     *
     * @author Shahraj Singh
     * @return list of ratings
     */
    @GetMapping("rating/deliverer/getall")
    public List<Rating> getDelivererRating(){
        return ratingService.getDelivererRating();
    }

    /**
     * Get all sender ratings
     *
     * @author Shahraj Singh
     * @return list of ratings
     */
    @GetMapping("rating/posted/getall")
    public List<Rating> getSenderPostedRating(){
        return ratingService.getSenderPostedRating();
    }

    /**
     * Delete rating
     *
     * @author Shahraj Singh
     * @param req request
     * @return response
     */
    @DeleteMapping("rating/delete")
    public String deleteRating(@RequestBody Map<String,String> req){
        return ratingService.deleteRating(Integer.valueOf(req.get("rating_id")));
    }

    /**
     * Get all ratings for a sender
     *
     * @author Shahraj Singh
     * @param driver_id driver id
     * @return list of ratings
     */
    @GetMapping("rating/deliverer")
    public List<Rating> getDelivererRatingById(@RequestParam int driver_id){
        return ratingService.getDelivererRatingWithID(driver_id);
    }
}
