package fr.kyo.crkf_web.security;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;


public class Email {

    private Email(){}

    public static void sendEmail(String toEmail, String subject, String body){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mailtrap.io"); //SMTP Host
        props.put("mail.smtp.port", "465"); //SMTP Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.ssl.checkserveridentity", true);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("0c7b763ff34c14", "bd15373faeace3");
            }
        };

        Session session = Session.getDefaultInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("from@test.fr", "NoReply"));
            msg.setReplyTo(InternetAddress.parse("replyto@test.fr", false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}