package esprit.tn.service;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface EmailService {
    void send(String to, String subject, String content);

    @Async
    void sendEmaiListUsers(List<String> toList, String subject, String content);

    String buildEmailVerifyMail(String token);
    String buildDemandeBody( ) ;
    String buildDemandeAdminBody( ) ;
    String buildResetPasswordMail(String token);
}
