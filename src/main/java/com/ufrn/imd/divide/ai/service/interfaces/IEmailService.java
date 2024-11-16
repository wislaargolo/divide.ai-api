package com.ufrn.imd.divide.ai.service.interfaces;

import org.springframework.mail.javamail.JavaMailSender;

public interface IEmailService {
    void sendEmail(String to, String subject, String htmlContent);

}
