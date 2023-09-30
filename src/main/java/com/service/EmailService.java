package com.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;


@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail() throws MessagingException, IOException {
//        Model model = new Model();
//        templateEngine.process

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true);

        mimeMessageHelper.setFrom("emtithal@gmail.com");
        mimeMessageHelper.setTo("ebnabanmy@gmail.com");
        mimeMessageHelper.setSubject("Test Email sender, and change mail");
        mimeMessageHelper.setText(htmlToString("src/main/resources/templates/mail/activationEmail.html"), true);
//        mimeMessageHelper.setText();

        javaMailSender.send(message);
    }

    private String htmlToString(String filePath) throws IOException {
        String htmlContent = FileUtils.readFileToString(new File(filePath));
        return htmlContent;    }
}
