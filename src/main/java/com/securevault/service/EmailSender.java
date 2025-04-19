//package com.securevault.service;

//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;

// Suppression de la classe EmailSender
// @Service
// public class EmailSender {
//     private final JavaMailSender emailSender;

//     public EmailSender(JavaMailSender emailSender) {
//         this.emailSender = emailSender;
//     }

//     public void sendEmail(String to, String subject, String text) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject(subject);
//         message.setText(text);
//         emailSender.send(message);
//     }
// }
