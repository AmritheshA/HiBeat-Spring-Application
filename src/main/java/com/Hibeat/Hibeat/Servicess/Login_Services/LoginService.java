package com.Hibeat.Hibeat.Servicess.Login_Services;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface LoginService {
    String login();

    String signUp(DTO userInfo, HttpSession session);


    String verifyEmail(String otp, Model model);

    String verifyOtp(String otp, Model model);

    String logout(HttpServletRequest request, HttpServletResponse response);

}
