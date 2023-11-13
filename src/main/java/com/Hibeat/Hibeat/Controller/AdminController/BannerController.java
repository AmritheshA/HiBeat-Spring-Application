package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.BannerDTO;
import com.Hibeat.Hibeat.Servicess.Admin_Service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class BannerController {

    private final BannerService bannerService;

    @Autowired
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/banner")
    public String banner(Model model){
        return bannerService.banners(model);
    }

    @PostMapping("/add-banner")
    public String addBanner(BannerDTO bannerDTO){
        return bannerService.addBanner(bannerDTO);
    }


    @GetMapping("/edit-banner")
    public String editBanner(Model model,
                             @RequestParam("id") Integer bannerId){
        return bannerService.editBanner(model,bannerId);
    }

    @PostMapping("/edit-banner")
    public String editBanners(BannerDTO bannerDTO,
                              @RequestParam("id") Integer bannerId){
        return bannerService.editBanner(bannerDTO,bannerId);
    }

    @GetMapping("/remove-banner")
    public String removeBanner(@RequestParam("id") Integer bannerId){
        bannerService.removeBanner(bannerId);
        return "redirect:/admin/banner";
    }

}
