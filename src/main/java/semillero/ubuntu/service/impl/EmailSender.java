package semillero.ubuntu.service.impl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.Map;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

@Service
@EnableAutoConfiguration
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendEmail(String destinatario, String asunto, String template, Map<String, Object> message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        Context context = new Context();
        context.setVariables(message);

        String contenido = templateEngine.process(template, context);
        System.out.println(contenido);

//        try {
//            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            mimeMessageHelper.setTo(destinatario);
//            mimeMessageHelper.setSubject(asunto);
//
//            String contenido = templateEngine.process(template, context);
//            mimeMessageHelper.setText(contenido, true);
//
//            javaMailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            // Manejar la excepción apropiadamente
//            e.printStackTrace();
//        }
    }
}

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@EnableAutoConfiguration
//public class EmailSender {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        javaMailSender.send(message);
//    }
//}
