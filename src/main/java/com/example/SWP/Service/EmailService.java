package com.example.SWP.Service;

import com.example.SWP.config.ThymeleafConfig;
import com.example.SWP.model.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@Service
public class EmailService {

    @Autowired
    @Qualifier("customTemplateEngine")
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendSimpleMessage(MailBody mailBody) {
        try {


            Context context = new Context();
            context.setVariable("name",mailBody.getTo().getEmail());
            Date expirationTime = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
            mailBody.setExpirationTime(expirationTime);
            context.setVariable("Date", mailBody.getExpirationTime());
            context.setVariable("otp",mailBody.getOtp());


            String template = templateEngine.process("OTP", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("koifish669@gmail.com");
            mimeMessageHelper.setTo(mailBody.getTo().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(mailBody.getSubject());



            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL");

        }

    }

    public void sendSimpleMessage2(MailBody mailBody) {
        try {

            Context context = new Context();
            context.setVariable("name",mailBody.getTo().getEmail());



            String template = templateEngine.process("REGIS", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("koifish669@gmail.com");
            mimeMessageHelper.setTo(mailBody.getTo().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(mailBody.getSubject());



            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL");

        }

    }
}
