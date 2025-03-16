package br.com.foursales.email.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class GmailService {

    @Autowired
    private JavaMailSender mailSender;
    public void enviarEmail(String destinatario, String assunto, String corpo) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("seu-email@sua-empresa.com"); // Deve coincidir com spring.mail.username
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(corpo);

            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso para " + destinatario);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        GmailService g = new GmailService();
        g.enviarEmail("franciscomiguel2003@gmail.com","Novo teste assunto", "teste corpo");
    }
}