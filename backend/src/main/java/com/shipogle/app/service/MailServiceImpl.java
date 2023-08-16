package com.shipogle.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
/*
* Reference: https://www.baeldung.com/spring-email
* Reference: https://stackoverflow.com/questions/33015900/no-mimemessage-content-exception-when-sending-simplemailmessage
*/

@Component
public class MailServiceImpl extends SimpleMailMessage {

    @Autowired
    JavaMailSender sender;

    /**
     * Send email to user
     *
     * @author Nandkumar Kadivar
     * @param email user email
     * @param subject email subject
     * @param body email body
     * @param link link that needs to be shared in email
     */
    public void sendMail(String email, String subject, String body ,String link){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("shipogleApp@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body+" "+link);
        sender.send(simpleMailMessage);
    }
}
