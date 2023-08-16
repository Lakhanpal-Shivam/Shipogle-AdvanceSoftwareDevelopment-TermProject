package com.shipogle.app.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shipogle.app.model.JwtToken;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.JwtTokenRepository;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.shipogle.app.utility.Const.SECRET_KEY;
import static com.shipogle.app.utility.Const.TOKEN_EXPIRATION_TIME;

/*
* Reference:https://www.viralpatel.net/java-create-validate-jwt-token/
* Reference: https://jwt.io/introduction
*/
@Service
public class JwtTokenServiceImpl implements JwtTokenService{

    @Autowired
    JwtTokenRepository jwtTokenRepo;

    /**
     * Create jwt token for user and set status of token
     *
     * @author Nandkumar Kadivar
     * @param user user.
     * @return String jwt token for that user.
     */
    public JwtToken createJwtToken(User user) {
        JwtToken token = new JwtToken();

        JwtBuilder jwtBuilder = Jwts.builder().claim("email", user.getEmail());
        jwtBuilder.setSubject(user.getFirst_name());
        jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
        jwtBuilder.setExpiration(Date.from(Instant.now().plus(TOKEN_EXPIRATION_TIME, ChronoUnit.MINUTES)));
        jwtBuilder.signWith(generateKey());
        String jwt_token = jwtBuilder.compact();

        token.setToken(jwt_token);
        token.setIs_active(true);
        token.setUser(user);
        return token;
    }

    /**
     * Generate key for token
     *
     * @author Nandkumar Kadivar
     * @return String secret key.
     */
    public Key generateKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Generate key for token
     *
     * @author Nandkumar Kadivar
     * @return String secret key.
     */
    public void deactiveUserTokens(User user) {
        List<JwtToken> activeTokens = jwtTokenRepo.getAllByUser(user);
        for (JwtToken t : activeTokens) {
            t.setIs_active(false);
        }
        jwtTokenRepo.saveAll(activeTokens);
    }

    /**
     * Resturn activation status of jwt
     *
     * @author Nandkumar Kadivar
     * @return boolean status.
     */
    public boolean isJwtActive(String token) {
        JwtToken jwt_token = jwtTokenRepo.getJwtTokensByToken(token);

        return jwt_token.getIs_active();
    }
}
