package com.shipogle.app.controller;

import com.shipogle.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.shipogle.app.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * Register user
     *
     * @author Nandkumar Kadivar
     * @param user user.
     * @return String response message.
     */
    @PostMapping("/register")
    public String registerNewUser(@RequestBody User user) {
        return authService.register(user);
    }

    /**
     * User login
     *
     * @author Nandkumar Kadivar
     * @param json json request.
     * @return String jwt token.
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> json) {
        long start = System.currentTimeMillis();
        String res = authService.login(json.get("email"), json.get("password"));
        System.out.println("Time difference: "+ (System.currentTimeMillis() - start));
        return res;
    }

    /**
     * Change password
     *
     * @author Nandkumar Kadivar
     * @param json json request.
     * @return String response message.
     */
    @PostMapping("/changepassword")
    public String changePassword(@RequestBody Map<String, String> json) {
        return authService.resetPassword(json.get("token"), json.get("password"));
    }

    /**
     * Forgot password
     *
     * @author Nandkumar Kadivar
     * @param json json request.
     * @return String response message.
     */
    @PostMapping("/forgotpassword")
    public String forgotPassword(HttpServletRequest request, @RequestBody Map<String, String> json) {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        return authService.forgotPassword(json.get("email"));
    }

    /**
     * Email verification
     *
     * @author Nandkumar Kadivar
     * @param code string verification code.
     * @param id int user id.
     * @return String response message.
     */
    @GetMapping("/verification")
    public String emailVerification(@RequestParam("code") String code, @RequestParam("id") int id) {
        return authService.verifyEmail(code, id);
    }

    /**
     * Email verification
     *
     * @author Rahul Saliya
     * @param id int user id.
     * @return String response message.
     */
    @GetMapping("/user")
    public String getUser(@RequestParam("id") int id) {
        System.out.println("id = " + id);
        return authService.getUser(id).toString();
    }

    /**
     * User information
     *
     * @author Rahul Saliya
     * @param token string jwt token.
     * @return User user object.
     */
    @GetMapping("/user_info")
    public User getUserInfo(@RequestHeader("Authorization") String token) {
        return authService.getUserInfo(token);
    }

    /**
     * Email verification
     *
     * @author Rahul Saliya
     * @param token string jwt token.
     * @param user user object
     * @return String response message.
     */
    @PutMapping("/user")
    public String updateUser(@RequestHeader("Authorization") String token, @RequestBody User user) {
        return authService.updateUser(token, user);
    }
}
