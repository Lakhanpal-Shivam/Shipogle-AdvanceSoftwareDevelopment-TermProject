package com.shipogle.app.service;

import com.shipogle.app.model.Rating;

import java.util.List;

public interface RatingService {
    public String storeRating(Long driver_route_id, float star, String review);

    public String deleteRating(Integer rating_id);

    public List<Rating> getDelivererRating();

    public List<Rating> getSenderPostedRating();

    public List<Rating> getDelivererRatingWithID(Integer id);
}
