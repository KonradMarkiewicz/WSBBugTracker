package com.wsb.WSBBugTracker.mails;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    final public JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(Mail mail){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setTo(mail.recipient);
            helper.setSubject(mail.subject);
            helper.setText(mail.content);

            javaMailSender.send(mimeMessage);

        } catch (Exception e){
            System.out.println("Coś poszło nie tak: ");
            e.printStackTrace();
        }
    }
}
