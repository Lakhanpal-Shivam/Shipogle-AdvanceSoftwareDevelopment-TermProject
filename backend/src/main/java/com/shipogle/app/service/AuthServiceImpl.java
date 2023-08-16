package com.shipogle.app.service;

import com.shipogle.app.model.ForgotPasswordToken;
import com.shipogle.app.model.User;
import com.shipogle.app.model.JwtToken;
import com.shipogle.app.repository.ForgotPasswordTokenRepository;
import com.shipogle.app.repository.JwtTokenRepository;
import com.shipogle.app.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.shipogle.app.utility.Const.*;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtTokenRepository jwtTokenRepo;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    MailServiceImpl mailService;
    @Autowired
    ForgotPasswordTokenRepository forgotPasswordTokenRepo;
    @Autowired
    ForgotPasswordTokenService forgotPasswordTokenService;

    /**
     * Check that user is already registered on not
     *
     * @author Nandkumar Kadivar
     * @param user user.
     * @return boolean value.
     */
    @Override
    public boolean isAlreadyExist(User user) {
        User db_user = userRepo.findUserByEmail(user.getEmail());
        return db_user != null;
    }

    /**
     * Reset password for user
     *
     * @author Nandkumar Kadivar
     * @param token reset password token.
     * @param password new password string.
     * @return String message.
     */
    @Override
    public String resetPassword(String token, String password) {
        try {
            ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepo.findByForgetPasswordToken(token);
            if (forgotPasswordToken.getIs_active()) {
                Claims claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
                String email = (String) claim.get("email");

                User user = userRepo.getUserByEmail(email);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String new_password = encoder.encode(password);
                user.setPassword(new_password);
                userRepo.save(user);
                forgotPasswordToken.setIs_active(false);
                forgotPasswordTokenRepo.save(forgotPasswordToken);

                return "Password changed successfully";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link is not active");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Send reset password email to user
     *
     * @author Nandkumar Kadivar
     * @param email email string.
     * @return String response message.
     */
    @Override
    public String forgotPassword(String email) {
        try {
            User user = userRepo.getUserByEmail(email);

            ForgotPasswordToken token = forgotPasswordTokenService.createForgotPasswordToken(user);
            String forgot_password_token = token.getForgot_password_token();

            String user_email = user.getEmail();
            String subject = "Reset Password";
            String body = "Password rest link(Expires in 24 hours): ";
            String reset_link =  URL_FRONTEND+"/forgotpwd/reset/"+forgot_password_token;
            mailService.sendMail(user_email, subject, body, reset_link);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return "Password reset link sent";
    }

    /**
     * Verify user email
     *
     * @author Nandkumar Kadivar
     * @param code verification code.
     * @param id user id.
     * @return String response message.
     */
    @Override
    public String verifyEmail(String code, int id) {

        try {
            User user = userRepo.getById(id);

            if (!user.getIs_verified()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(user.getEmail(), code)) {
                    user.setIs_verified(true);
                    userRepo.save(user);
                    return "Email Verified";
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not valid user");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not valid user");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Register user and send verification email
     *
     * @author Nandkumar Kadivar
     * @param new_user user.
     * @return String response message.
     */
    @Override
    public String register(User new_user) {
        if (!isAlreadyExist(new_user)) {
            String user_password = new_user.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            new_user.setPassword(encoder.encode(user_password));
            new_user.setIs_verified(false);
            userRepo.save(new_user);

            String encoded_email = encoder.encode(new_user.getEmail());

            String user_email = new_user.getEmail();
            String subject = "Email Verification";
            String body = "Please verify your email:";
            String link =  URL_BACKEND+"/verification?code="+encoded_email+"&id="+new_user.getUser_id();

            mailService.sendMail(user_email,subject, body, link);

            return "Verification email sent";

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already exist with this email");
        }
    }

    /**
     * User login
     *
     * @author Nandkumar Kadivar
     * @param email user email.
     * @param password user password.
     * @return String jwt token for that user.
     */
    @Override
    public String login(String email, String password) {

        UsernamePasswordAuthenticationToken auth_token = new UsernamePasswordAuthenticationToken(email, password);
        System.out.println(auth_token);
        try {
            authManager.authenticate(auth_token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        User storedUser = userRepo.getUserByEmail(email);

        if (storedUser.getIs_verified()) {
            JwtToken token = jwtTokenService.createJwtToken(storedUser);
            jwtTokenService.deactiveUserTokens(storedUser);
            jwtTokenRepo.save(token);
            return token.getToken();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not verified");
        }
    }

    /**
     * get user by id
     *
     * @author Rahul Saliya
     * @param id user id.
     * @return boolean true if user already exist.
     */
    @Override
    public User getUser(int id) {
        return userRepo.getReferenceById(id);
    }

    /**
     * get user by token
     *
     * @author Rahul Saliya
     * @param token jwt token.
     * @return boolean true if user already exist.
     */
    @Override
    public User getUserInfo(String token) {
        token = token.replace("Bearer", "").trim();
        Claims claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String email = (String) claim.get("email");
        return userRepo.getUserByEmail(email);
    }

    /**
     * update user by token
     *
     * @author Rahul Saliya
     * @param user user object.
     * @return boolean true if user already exist.
     */
    @Override
    public String updateUser(String token, User user) {
        token = token.replace("Bearer", "").trim();
        Claims claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String email = (String) claim.get("email");
        User db_user = userRepo.getUserByEmail(email);
        db_user.update(user);
        userRepo.save(db_user);
        return "User updated";
    }
}
