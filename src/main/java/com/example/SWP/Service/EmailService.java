package com.example.SWP.Service;

import com.example.SWP.model.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(mailBody.getTo().getEmail());
            mimeMessageHelper.setFrom("koifish669@gmail.com");
            mimeMessageHelper.setSubject(mailBody.getSubject());
            mimeMessageHelper.setText(mailBody.getLink());


            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL");

        }

    }
}
