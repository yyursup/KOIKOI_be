package com.example.SWP.Service;

import com.example.SWP.config.ThymeleafConfig;
import com.example.SWP.entity.Orders;
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
            context.setVariable("name", mailBody.getTo().getEmail());
            context.setVariable("link", mailBody.getLink()); // Truyền link vào context

            String template = templateEngine.process("RESETPASSWORD", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(mailBody.getTo().getEmail());
            mimeMessageHelper.setText(template, true); // Định dạng HTML cho nội dung email
            mimeMessageHelper.setSubject(mailBody.getSubject());

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL");
        }
    }



    public void sendOrderConfirmationEmail (Orders order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomer_Email());
        message.setSubject("Xác nhận đặt hàng thành công");

        String body = "Xin chào " + order.getCustomer_Name() + ",\n\n" +
                "Cảm ơn bạn đã đặt hàng. Dưới đây là thông tin đơn hàng của bạn:\n" +
                "Tên sản phẩm: " + order.getProduct_Name() + "\n" +
                "Số lượng: " + order.getQuantity() + "\n" +
                "Giá: " + order.getPrice() + " VND\n\n" +
                "Chúng tôi sẽ liên hệ với bạn sớm để xác nhận đơn hàng.\n" +
                "Cảm ơn bạn đã mua sắm tại cửa hàng chúng tôi!\n";

        message.setText(body);
        message.setFrom("koifish669@gmail.com");

        javaMailSender.send(message);
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

    //mail gia hạn ngày kí gử
    public void sendExpiryNotification(MailBody mailBody, Context context) {
        String recipientEmail = mailBody.getTo().getEmail(); // Lấy email từ Account
        String subject = mailBody.getSubject();
        try {
            // Tạo nội dung email từ template "ExpiryNotification"
            String template = templateEngine.process("notifyConsignmentExpiry", context);

            // Tạo đối tượng MimeMessage và thiết lập thông tin
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("koifish669@gmail.com");
            mimeMessageHelper.setTo(recipientEmail); // Sử dụng recipientEmail đã lấy ở trên
            mimeMessageHelper.setText(template, true); // Cho phép nội dung HTML
            mimeMessageHelper.setSubject(subject);

            // Gửi email
            javaMailSender.send(mimeMessage);
            System.out.println("Email sent to: " + recipientEmail); // Log xác nhận gửi thành công

        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL to: " + recipientEmail);
            e.printStackTrace(); // Hiển thị chi tiết lỗi
        }
    }
}
