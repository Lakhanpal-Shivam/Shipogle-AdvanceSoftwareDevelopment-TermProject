package com.shipogle.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class MailServiceTests {
    @InjectMocks
    MailServiceImpl mailService;

    @Mock
    JavaMailSender sender;

    @Test
    public void sendMailTest(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("shipogleApp@gmail.com");
        simpleMailMessage.setTo("kadivarnand007@gmail.com");
        simpleMailMessage.setSubject("subject");
        simpleMailMessage.setText("body link");
        mailService.sendMail("kadivarnand007@gmail.com","subject","body","link");

        Mockito.verify(sender,Mockito.times(1)).send(simpleMailMessage);
    }
}
