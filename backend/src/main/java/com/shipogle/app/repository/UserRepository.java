package com.shipogle.app.repository;

import com.shipogle.app.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * getUserByEmail is a method to get user by email
     *
     * @author Nandkumar Kadivar
     * @param email email
     * @return User object
     */
    User getUserByEmail(String email);

    /**
     * findUserByEmail is a method to find user by email
     *
     * @author Nandkumar Kadivar
     * @param email email
     * @return User object
     */
    User findUserByEmail(String email);

    /**
     * findByEmail is a method to find user by email
     *
     * @author Nandkumar Kadivar
     * @param email email
     * @return User object
     */
    User findByEmail(String email);

    /**
     * getUserById is a method to get user by id
     *
     * @author Nandkumar Kadivar
     * @param id user id
     * @return User object
     */
    User getUserById(Integer id);

    /**
     * getUserByIds is a method to get user by ids
     *
     * @author Rahul Saliya
     * @param userIds user ids
     * @return List<User>
     */
    @Query("SELECT u FROM User u WHERE u.id in :userIds")
    List<User> getUserByIds(@Param("userIds") List<Integer> userIds);

}
