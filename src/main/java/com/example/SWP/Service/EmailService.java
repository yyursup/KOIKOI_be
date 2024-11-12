package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Orders;
import com.example.SWP.entity.Transactions;
import com.example.SWP.model.MailBody;
import com.example.SWP.model.request.TransactionRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    @Qualifier("customTemplateEngine")
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AccountRepository accountRepository;

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


    public void sendConsignSuccessNotification(MailBody mailBody, Context context) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            if (mailBody == null || mailBody.getTo() == null || mailBody.getTo().getEmail() == null) {
                throw new IllegalArgumentException("Invalid MailBody: recipient email is missing");
            }

            String template = templateEngine.process("ConsignSuccessNotification", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom("koifish669@gmail.com");
            mimeMessageHelper.setTo(mailBody.getTo().getEmail());
            mimeMessageHelper.setSubject(mailBody.getSubject());
            mimeMessageHelper.setText(template, true);

            javaMailSender.send(mimeMessage);
            logger.info("Consignment success email sent to: {}", mailBody.getTo().getEmail());

        } catch (MessagingException e) {
            logger.error("Failed to send consignment success email to: {}", mailBody.getTo().getEmail(), e);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid email details: ", e);
        } catch (Exception e) {
            logger.error("Unexpected error in sendConsignSuccessNotification", e);
        }
    }

    // Gửi thông báo rút tiền thành công
    public void sendWithdrawalConfirmationEmail(Account account, double amount, TransactionRequest transactionRequest) {
        Context context = new Context();
        context.setVariable("name", account.getEmail());
        context.setVariable("amount", amount);
        context.setVariable("accountNumber", transactionRequest.getAccountNumber());
        context.setVariable("accountName", transactionRequest.getAccountName());
        context.setVariable("bankName", transactionRequest.getBankName());

        String template = templateEngine.process("withdrawalConfirmation", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(account.getEmail());
            mimeMessageHelper.setSubject("Withdrawal Successful");
            mimeMessageHelper.setText(template, true); // Cho phép HTML
            mimeMessageHelper.setFrom("koifish669@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e.getMessage());
        }
    }


    // Gửi thông báo yêu cầu xác nhận rút tiền đến Manager
    public void sendPendingWithdrawalRequestToManager(Transactions transaction) {
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);

        Context context = new Context();
        context.setVariable("amount", transaction.getTotalAmount());
        context.setVariable("userEmail", transaction.getFrom().getEmail());
        context.setVariable("accountNumber", transaction.getAccountNumber());
        context.setVariable("accountName", transaction.getAccountName());
        context.setVariable("bankName", transaction.getBankName());

        String template = templateEngine.process("pendingWithdrawalRequest", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(manager.getEmail());
            mimeMessageHelper.setSubject("Pending Withdrawal Request");
            mimeMessageHelper.setText(template, true); // Cho phép HTML
            mimeMessageHelper.setFrom("koifish669@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e.getMessage());
        }
    }


    public void sendWithdrawalRejectionEmail(Account account, double amount, TransactionRequest transactionRequest) {
        Context context = new Context();
        context.setVariable("name", account.getEmail());
        context.setVariable("amount", amount);
        context.setVariable("accountNumber", transactionRequest.getAccountNumber());
        context.setVariable("accountName", transactionRequest.getAccountName());
        context.setVariable("bankName", transactionRequest.getBankName());

        String template = templateEngine.process("withdrawalRejection", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(account.getEmail());
            mimeMessageHelper.setSubject("Withdrawal Request Rejected");
            mimeMessageHelper.setText(template, true); // Cho phép HTML
            mimeMessageHelper.setFrom("koifish669@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e.getMessage());
        }
    }



}
