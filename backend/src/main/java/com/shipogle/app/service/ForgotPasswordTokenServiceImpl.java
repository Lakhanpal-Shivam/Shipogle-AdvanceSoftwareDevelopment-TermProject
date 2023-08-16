package com.shipogle.app.service;

import com.shipogle.app.model.ForgotPasswordToken;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.ForgotPasswordTokenRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static com.shipogle.app.utility.Const.SECRET_KEY;
import static com.shipogle.app.utility.Const.TOKEN_EXPIRATION_TIME;

@Service
public class ForgotPasswordTokenServiceImpl implements ForgotPasswordTokenService {

    @Autowired
    ForgotPasswordTokenRepository forgotPasswordTokenRepo;

    /**
     * Create forgot password jwt token for user
     *
     * @author Nandkumar Kadivar
     * @param user user.
     * @return String jwt token for that user.
     */
    @Override
    public ForgotPasswordToken createForgotPasswordToken(User user){
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        JwtBuilder jwtBuilder = Jwts.builder().claim("email",user.getEmail());
        jwtBuilder.setSubject(user.getFirst_name());
        jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
        jwtBuilder.setExpiration(Date.from(Instant.now().plus(TOKEN_EXPIRATION_TIME, ChronoUnit.MINUTES)));
        jwtBuilder.signWith(generateKey());
        String forgot_password_token = jwtBuilder.compact();

        forgotPasswordToken.setForgot_password_token(forgot_password_token);
        forgotPasswordToken.setIs_active(true);
        forgotPasswordToken.setUser(user);
        forgotPasswordTokenRepo.save(forgotPasswordToken);

        return forgotPasswordToken;
    }

    /**
     * Generate key for token
     *
     * @author Nandkumar Kadivar
     * @return String secret key.
     */
    @Override
    public Key generateKey(){
        return new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());
    }
}
