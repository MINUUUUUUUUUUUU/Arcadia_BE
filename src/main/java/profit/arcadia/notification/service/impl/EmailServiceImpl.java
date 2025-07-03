package profit.arcadia.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import profit.arcadia.notification.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private String authNum; // 인증번호

    // 인증번호 8자리 무작위 생성
    private void createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97)); // a-z
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65)); // A-Z
                    break;
                case 2:
                    key.append(random.nextInt(10)); // 0-9
                    break;
            }
        }

        authNum = key.toString();
    }

    @Override
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode();
        String setFrom = "testtest12@gmail.com";
        String title = "데옹 인증번호 테스트";

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);

        String msgOfEmail = "";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 Arcardia 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>" + authNum + "</strong><div><br/>";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }

    @Override
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(email);
        emailSender.send(emailForm);
        return authNum;
    }
}
