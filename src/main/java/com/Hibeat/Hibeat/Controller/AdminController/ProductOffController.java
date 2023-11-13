package com.Hibeat.Hibeat.Controller.AdminController;


import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ProductOfferDTO;
import com.Hibeat.Hibeat.Servicess.Admin_Service.ProductOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class ProductOffController {

    private final ProductOfferService productOfferService;

    @Autowired
    public ProductOffController(ProductOfferService productOfferService) {
        this.productOfferService = productOfferService;
    }

    @GetMapping("/product-off")
    public String productOffer(Model model){
        return productOfferService.productOffers(model);
    }


    @PostMapping("/add-productOff")
    public String addProductOffer(@ModelAttribute ProductOfferDTO productOffers){
        return productOfferService.addProductOffer(productOffers);
    }

    @PostMapping("/edit-productOff")
    public String editProduct(@ModelAttribute ProductOfferDTO productOfferDTO){
        return productOfferService.editProductOff(productOfferDTO);
    }

    @GetMapping("/remove-offer")
    public String removeOffer(@RequestParam("id") Integer productId){
        return productOfferService.removeProductOffer(productId);
    }
}
