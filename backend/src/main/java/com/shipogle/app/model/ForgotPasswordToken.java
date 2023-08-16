package com.shipogle.app.model;

import javax.persistence.*;

/**
 * Forgot password token model.
 *
 * @author Nandkumar Kadivar
 */
@Entity
@Table(name="forgot_password_token")
public class ForgotPasswordToken {
    @Id
    @GeneratedValue
    @Column(name = "forgot_password_token_id")
    private Integer forgot_password_token_id;
    @Column(name = "forgetPasswordToken")
    private String forgetPasswordToken;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "USER_ID",referencedColumnName = "user_id")
    private User user;

    @Column(name = "is_active")
    private Boolean is_active;

    public Integer getForgot_password_token_id() {
        return forgot_password_token_id;
    }

    public void setForgot_password_token_id(Integer forgot_password_token_id) {
        this.forgot_password_token_id = forgot_password_token_id;
    }

    public String getForgot_password_token() {
        return forgetPasswordToken;
    }

    public void setForgot_password_token(String forgot_password_token) {
        this.forgetPasswordToken = forgot_password_token;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
