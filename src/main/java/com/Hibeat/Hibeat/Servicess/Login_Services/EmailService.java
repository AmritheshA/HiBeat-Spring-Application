package com.Hibeat.Hibeat.Servicess.Login_Services;

import com.Hibeat.Hibeat.Model.Admin;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.AdminRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class EmailService {

    JavaMailSender javaMailSender;
    HttpSession session;
    UserRepository userRepository;

    ModelMapperConverter modelMapperConverter;
    PasswordEncoder passwordEncoder;

    AdminRepository adminRepository;

    @Autowired
    public EmailService(JavaMailSender javaMailSender,
                        HttpSession session,
                        UserRepository userRepository,
                        ModelMapperConverter modelMapperConverter,
                        PasswordEncoder passwordEncoder,
                        AdminRepository adminRepository) {
        this.javaMailSender = javaMailSender;
        this.session = session;
        this.userRepository = userRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public void sendEmails(String to){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("OTP Verification");
        message.setText("OTP"+session.getAttribute("otp"));
        session.setAttribute("email",to);
        javaMailSender.send(message);
    }
    public String otpGenerator(){

        Random random = new Random();
        String otp = (String.valueOf(random.nextInt(9999 - 1000) + 1000));
        session.setAttribute("otp",otp);
        session.setAttribute("otpCreatedTime", Instant.now().toEpochMilli());

        return otp;
    }

    public Boolean isOTPExpired() {

        long currentTimeInMillis = Instant.now().toEpochMilli();
        long otpCreationTimeInMillis = (long) session.getAttribute("otpCreatedTime");
        return (otpCreationTimeInMillis + (0.2 * 60 * 1000)) < currentTimeInMillis;
    }

//    This Is For Checking OTP of Forgot Password
    public Boolean isEmailVerified(String enteredOtp){

        String otp = (String) session.getAttribute("otp");
        if(otp.equals(enteredOtp)){
            return true;
        }
        return false;
    }


//    This Is For Checking Email Is Verified When User Is Signing
    public Boolean isOTPVerified(String enteredOtp){

        String otp = (String) session.getAttribute("otp");
        if(otp.equals(enteredOtp)){
            System.out.println("At Otp Verified");
//            After Successful Verification UserInfo is Going to save with Encrypted Password
            User userInfo = modelMapperConverter.DTOToUser((DTO) session.getAttribute("userInfo"));
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfo.setRole("user");
            userInfo.setCreate_date(getCurrentDate());
            userRepository.save((userInfo));


            return true;
        }
        return false;
    }
    public Date getCurrentDate() {
        return new Date(); // This returns the current date and time as a Date object
    }

}
