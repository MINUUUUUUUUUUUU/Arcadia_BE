package profit.arcadia.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    String sendEmail(String email) throws MessagingException, UnsupportedEncodingException;

    MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException;
}
