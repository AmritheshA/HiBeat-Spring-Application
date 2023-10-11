package com.Hibeat.Hibeat.Controller.loginController;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.Servicess.Login_Services.EmailService;
import com.Hibeat.Hibeat.Servicess.Login_Services.RestPasswordService;
import com.Hibeat.Hibeat.Servicess.User_Service.Services;
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

    @Autowired
    public LoginController(EmailService emailService,
                           RestPasswordService restPasswordService) {
        this.emailService = emailService;
        this.restPasswordService = restPasswordService;
    }


    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains("user")) {
                    return "redirect:/user/shop";
                }
            }
        }
        return "LoginRegistration/Login";
    }

    @GetMapping("/signUp")
    public String signups() {
        return "LoginRegistration/SignUp";
    }

    @PostMapping("/signUp")
    public String signup(@ModelAttribute DTO userInfo, HttpSession session) {
//    Assigning Userinfo to session
        session.setAttribute("userInfo", userInfo);

//    OTP Generation and Setting the to Mail
        emailService.otpGenerator();
        emailService.sendEmails(userInfo.getEmail());

        return "LoginRegistration/VerifyEmail";
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String otp, Model model) {

        if (emailService.isOTPVerified(otp)) {
            if (emailService.isOTPExpired()) {
                return "redirect:/user/shop";
            } else {
                model.addAttribute("error", "OTP is valid but has not expired yet.");
            }
        } else {
            model.addAttribute("error", "OTP is wrong. Please try again.");
        }
        return "LoginRegistration/VerifyEmail";
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

        if (emailService.isEmailVerified(otp)) {
            return "LoginRegistration/RestPassword-Continue";
        } else {
            model.addAttribute("error", "OTP is wrong. Please try again.");
        }
        return "redirect:/forgot";
    }

    @PostMapping("/resetOrContinue")
    public String handleFormSubmission(@RequestParam("action") String action) {
//        if user choose restPassword
        if ("resetPassword".equals(action)) {
            restPasswordService.initialPasswordRest();
            return "LoginRegistration/Login";
//Else Continue as Logged In
        }
//        lack of home page i use shop for Now...
        return "redirect:/user/shop";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
