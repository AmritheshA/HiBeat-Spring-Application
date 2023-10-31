package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Model.Wallet;
import com.Hibeat.Hibeat.Repository.UserRepository;
import com.Hibeat.Hibeat.Repository.WalletRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class WalletController {

    UserRepository userRepository;
    WalletRepository walletRepository;

    public WalletController(UserRepository userRepository,
                            WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @GetMapping("/wallet")
    public String wallet(Principal principal, Model model) {

        User user = userRepository.findByName(principal.getName());
        Wallet wallet = walletRepository.findByUser(user);

        model.addAttribute("wallet",wallet);
        return "User/wallet";
    }
}
