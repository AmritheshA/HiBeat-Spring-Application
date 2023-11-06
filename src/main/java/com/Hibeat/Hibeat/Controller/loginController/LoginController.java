package com.Hibeat.Hibeat.Controller.loginController;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.Repository.UserRepository;
import com.Hibeat.Hibeat.Servicess.Login_Services.EmailService;
import com.Hibeat.Hibeat.Servicess.Login_Services.LoginService;
import com.Hibeat.Hibeat.Servicess.Login_Services.RestPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final EmailService emailService;
    private final RestPasswordService restPasswordService;

    UserRepository userRepository;

    private final LoginService loginService;


    @Autowired
    public LoginController(EmailService emailService,
                           RestPasswordService restPasswordService,
                           UserRepository userRepository, LoginService loginService) {
        this.emailService = emailService;
        this.restPasswordService = restPasswordService;
        this.userRepository = userRepository;
        this.loginService = loginService;
    }



    @GetMapping("/login")
    public String login() {
       return loginService.login();
    }

    @GetMapping("/signUp")
    public String signups() {
        return "LoginRegistration/SignUp";
    }

    @PostMapping("/signUp")
    public String signup(@ModelAttribute DTO userInfo, HttpSession session) {
        return loginService.signUp(userInfo,session);
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String otp, Model model) {
      return loginService.verifyEmail(otp,model);
    }

    @GetMapping("/forgot")
    public String forgotPassword() {
        return "LoginRegistration/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswords(@RequestParam String email) {

        emailService.otpGenerator();
        emailService.sendEmails(email);
        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String verify_Otp() {
        return "LoginRegistration/VerifyUser";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, Model model) {
       return loginService.verifyOtp(otp,model);
    }

    @PostMapping("/resetOrContinue")
    public String handleFormSubmission(@RequestParam("action") String action) {
        restPasswordService.initialPasswordRest();
        return "LoginRegistration/Login";

    }

    @GetMapping("/reset-password")
    public String restPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "LoginRegistration/ResetPassword";

    }

    @PostMapping("/reset-passwords")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        restPasswordService.resetPassword(token, password);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return loginService.logout(request,response);
    }
}
