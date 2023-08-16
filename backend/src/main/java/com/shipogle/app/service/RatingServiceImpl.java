package com.shipogle.app.service;

import com.shipogle.app.model.DriverRoute;
import com.shipogle.app.model.Rating;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.DriverRouteRepository;
import com.shipogle.app.repository.RatingRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    RatingRepository ratingRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    DriverRouteRepository driverRouteRepo;
    @Autowired
    UserService userService;

    /**
     * Store rating
     *
     * @author Nandkumar Kadivar
     * @param driver_route_id driver route id
     * @param star star
     * @param review review
     * @return String rating is posted
     */
    @Override
    public String storeRating(Long driver_route_id, float star, String review){
        try {
            Rating rating = new Rating();
            DriverRoute driverRoute = driverRouteRepo.getDriverRouteById(driver_route_id);

            User sender = userService.getLoggedInUser();

            if(driverRoute==null || sender==null)
                return "Not able to post rating";

            rating.setUser(sender);
            rating.setDriverRoute(driverRoute);
            rating.setStar(star);
            rating.setReview(review);

            ratingRepo.save(rating);

        }catch (Exception e){
            return e.getMessage();
        }
        return "Rating is posted";
    }

    /**
     * Get rating
     *
     * @author Nandkumar Kadivar
     * @param rating_id rating id
     * @return List<Rating>
     */
    @Override
    public String deleteRating(Integer rating_id){
        try {
            Rating rating = ratingRepo.getRatingById(rating_id);

            ratingRepo.delete(rating);

        }catch (Exception e){
            return e.getMessage();
        }
        return "Rating deleted";
    }

    /**
     * Get deliverer rating
     *
     * @author Nandkumar Kadivar
     * @return List<Rating>
     */
    @Override
    public List<Rating> getDelivererRating(){
        try {

            User deliverer = userService.getLoggedInUser();

            List<Rating> ratings = ratingRepo.getAllByDriverRoute_DriverId(String.valueOf(deliverer.getUser_id()));

            return ratings;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Get sender posted rating
     *
     * @author Nandkumar Kadivar
     * @return List<Rating>
     */
    @Override
    public List<Rating> getSenderPostedRating(){
        try {
            User sender = userService.getLoggedInUser();

            List<Rating> ratings = ratingRepo.getAllByUser_Id(sender.getUser_id());

            return ratings;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Get deliverer rating with id
     *
     * @author Nandkumar Kadivar
     * @param id deliverer id
     * @return List<Rating>
     */
    @Override
    public List<Rating> getDelivererRatingWithID(Integer id){
        try {

            User deliverer = userRepo.getUserById(id);

            List<Rating> ratings = ratingRepo.getAllByDriverRoute_DriverId(String.valueOf(deliverer.getUser_id()));

            return ratings;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
