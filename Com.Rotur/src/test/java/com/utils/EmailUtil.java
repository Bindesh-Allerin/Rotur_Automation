package com.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    public static void sendReport() {

        final String from = "bindeshgupta144@gmail.com";
        final String password = "axxk lokq wpcl zgpp";

        String to = "nortondexter02@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject("Automation Test Report");

            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setText("Please find attached report.");

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(ExtentManager.reportPath);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email Sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
