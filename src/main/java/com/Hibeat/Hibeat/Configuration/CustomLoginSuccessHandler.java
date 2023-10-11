package com.Hibeat.Hibeat.Configuration;

import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {


    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    public CustomLoginSuccessHandler(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

//        If the user is authenticated By Oauth2 Service
//        I will collect all the information related to the user and SAVE
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Random random = new Random();

            String username = oauth2User.getAttribute("given_name");
            String email = oauth2User.getAttribute("email");
            String password = (String.valueOf(random.nextInt(99999999 - 10000000) + 10000000));
            Date date = new Date();
            String role = "user";

//        Here I am setting the related data
            User userInfo = new User();
            userInfo.setName(username);
            userInfo.setEmail(email);
            userInfo.setCreate_date(date);

            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(role));
            userInfo.setRole(authorities.toString());

            userInfo.setPassword(passwordEncoder.encode(password));

            // Authenticate the user and save to security Context
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

//        Saving the information to the database
            userRepository.save(userInfo);

            response.sendRedirect("/user/shop");

        }

        else if (roles.contains("super_admin")) {
            response.sendRedirect("/admin/products");
        }

        else if(roles.contains("user")){
//            Here the home page is required but the lack of pages i use shop to display
            response.sendRedirect("/user/shop");
        }else{
            throw new UsernameNotFoundException("User Is Not Found");
        }
    }

}
