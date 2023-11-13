package com.Hibeat.Hibeat.Servicess.Admin_Service;


import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.BannerDTO;
import org.springframework.ui.Model;

public interface BannerService {

    String banners(Model model);

    String addBanner(BannerDTO bannerDetails);

    Banner saveBanner(Banner banner);

    String editBanner(BannerDTO banner,Integer bannerId);

    String editBanner(Model model, Integer bannerId);

    void removeBanner(Integer bannerId);

}
