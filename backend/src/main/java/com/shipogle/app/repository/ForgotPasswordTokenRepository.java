package com.shipogle.app.repository;

import com.shipogle.app.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Integer> {

    /**
     * findByForgetPasswordToken is a method to get forgot password token by token
     *
     * @author Nandkumar Kadivar
     * @param Forgot_password_token forgot password token
     * @return ForgotPasswordToken object
     */
    ForgotPasswordToken findByForgetPasswordToken(String Forgot_password_token);
}
