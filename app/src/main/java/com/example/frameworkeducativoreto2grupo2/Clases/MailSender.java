package com.example.frameworkeducativoreto2grupo2.Clases;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    public static void sendEmail(final String recipientEmail, String contrasenaNueva) {
        final String emailFrom = "ayla.hernandezca@elorrieta-errekamari.com";
        final String contrasenaFrom = "pmijjvohytubbujf";  // creamos una Google App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, contrasenaFrom);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Restablecer contraseña");
            message.setText("Su nueva contraseña es: " + contrasenaNueva);

            Transport.send(message);
            System.out.println("Email Sent Successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
