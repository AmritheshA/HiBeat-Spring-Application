package com.Hibeat.Hibeat.Servicess.Login_Services;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Slf4j
public class LoginServiceImp implements LoginService {

    private final EmailService emailService;

    @Autowired
    public LoginServiceImp(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public String login() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                    if (userDetails.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("user")
                                    || grantedAuthority.getAuthority().equals("admin"))) {
                        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("user"))) {
                            return "redirect:/";
                        } else if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("super_admin"))) {
                            return "redirect:/admin/dashboard";
                        }
                    }
                }
            }
            return "LoginRegistration/Login";
        } catch (Exception e) {
            log.info("login " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String signUp(DTO userInfo, HttpSession session) {
        try {
//        Assigning Userinfo to session
            session.setAttribute("userInfo", userInfo);

//    OTP Generation and Setting the to Mail
            emailService.otpGenerator();
//            emailService.sendEmails(userInfo.getEmail());

            return "LoginRegistration/VerifyEmail";
        } catch (Exception e) {
            log.info("signUp " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String verifyEmail(String otp, Model model) {
        try {
            if (emailService.isOTPVerified(otp)) {
                if (emailService.isOTPExpired()) {
                    return "redirect:/";
                } else {
                    model.addAttribute("error", "OTP is wrong. Please try again.");
                }
            } else {
                model.addAttribute("error", "OTP is wrong. Please try again.");
            }
            return "LoginRegistration/VerifyEmail";
        } catch (Exception e) {
            log.info("verifyEmail " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String verifyOtp(String otp, Model model) {
        try {

            if (emailService.isEmailVerified(otp)) {
                return "LoginRegistration/RestPassword-Continue";
            } else {
                model.addAttribute("error", "OTP is wrong. Please try again.");
            }
            return "redirect:/forgot";
        } catch (Exception e) {
            log.info("verifyOtp " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/login?logout";
        } catch (Exception e) {
            log.info("logout " + e.getMessage());
            return "Exception/404";
        }
    }
}
