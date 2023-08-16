package com.shipogle.app.repository;

import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Integer> {

    /**
     * getAllByUser is a method to get all jwt tokens by user
     *
     * @author Nandkumar Kadivar
     * @param user user object
     * @return List<JwtToken>
     */
    List<JwtToken> getAllByUser(User user);

    /**
     * getJwtTokensByToken is a method to get jwt token by token
     *
     * @author Nandkumar Kadivar
     * @param token token
     * @return JwtToken object
     */
    JwtToken getJwtTokensByToken(String token);
}
