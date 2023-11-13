package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.BannerDTO;
import com.Hibeat.Hibeat.Repository.Admin.BannerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class BannerServiceImp implements BannerService {

    private final BannerRepository bannerRepository;

    @Autowired
    public BannerServiceImp(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @Override
    public String banners(Model model) {
        try {
            List<Banner> banners = bannerRepository.findAll();

            model.addAttribute("banners", banners);

            return "Admin/banner";
        } catch (Exception e) {
            log.info("banners " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String editBanner(BannerDTO bannerDetails, Integer bannerId) {

        try {
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";
            Banner banner = bannerRepository.findById(bannerId).get();

            banner.setBannerStatus(bannerDetails.getStatus());
            banner.setBannerTitle(bannerDetails.getTitle());
            if (bannerDetails.getImage() != null) {
                bannerDetails.getImage().transferTo(new File(file + bannerDetails.getImage().getOriginalFilename()));
                banner.setImage(bannerDetails.getImage().getOriginalFilename());
            }
            saveBanner(banner);
            return "redirect:/admin/banner";
        } catch (Exception e) {
            log.info("editBanner " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String editBanner(Model model, Integer bannerId) {
        try {
            Banner banner = bannerRepository.findById(bannerId).get();
            model.addAttribute("banner", banner);

            return "Admin/editBanner";
        } catch (Exception e) {
            log.info("editBanner " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public void removeBanner(Integer bannerId) {
        try {
            Banner banner = bannerRepository.findById(bannerId).get();
            bannerRepository.delete(banner);
        } catch (Exception e) {
            log.info("editBanner " + e.getMessage());
        }
    }


    @Override
    public String addBanner(BannerDTO bannerDetails) {

        try {
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

            Banner newBanner = new Banner();

            newBanner.setBannerStatus(bannerDetails.getStatus());
            newBanner.setBannerTitle(bannerDetails.getTitle());
            bannerDetails.getImage().transferTo(new File(file + bannerDetails.getImage().getOriginalFilename()));
            newBanner.setImage(bannerDetails.getImage().getOriginalFilename());

            saveBanner(newBanner);

            return "redirect:/admin/banner";
        } catch (Exception e) {
            log.info("banners " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

}
