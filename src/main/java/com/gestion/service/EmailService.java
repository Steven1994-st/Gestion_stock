package com.gestion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Send a simple email
     * @param receiver
     * @param subject
     * @param text
     */
    public void sendSimpleMessage(String receiver, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("projetglgrp9@gmail.com");
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
