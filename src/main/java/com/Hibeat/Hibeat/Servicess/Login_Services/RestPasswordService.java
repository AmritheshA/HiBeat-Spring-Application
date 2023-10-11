package com.Hibeat.Hibeat.Servicess.Login_Services;

import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestPasswordService {

    JavaMailSender javaMailSender;
    UserRepository userRepository;
    HttpSession session;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RestPasswordService(JavaMailSender javaMailSender,
                               UserRepository userRepository,
                               HttpSession session,
                               PasswordEncoder passwordEncoder){
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.session = session;
        this.passwordEncoder = passwordEncoder;
    }


//    In This Method RestToken Was Generated and Saving it into the dataBase,
//    and Sending the mail with restLink
    public void initialPasswordRest(){
        String email = (String) session.getAttribute("email");
        User user = userRepository.findByEmail(email);


        if(user!= null){

            String resetToken = generateResetToken();
            user.setResetToken(resetToken);
            userRepository.save(user);

            String restLink = "http://localhost:8080/reset-password?token=" + resetToken;
            sendResetPasswordEmail(email,restLink);

        }
    }

// Update the user's password,
// and reset token to null and saving it into database
    public void  resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);
            System.out.println("Password is rested..");
        }
    }

    public String generateResetToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

//  This Method is responsible for sending Email for the user with RestLink
    private void sendResetPasswordEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset");
        message.setText("To reset your password, click the following link: " + resetLink);
        javaMailSender.send(message);
    }

}
