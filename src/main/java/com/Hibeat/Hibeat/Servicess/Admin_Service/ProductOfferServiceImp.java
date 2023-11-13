package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.ProductOffers;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ProductOfferDTO;
import com.Hibeat.Hibeat.Repository.Admin.ProductOfferRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProductOfferServiceImp implements ProductOfferService{

    private final ProductOfferRepository productOfferRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductOfferServiceImp(ProductOfferRepository productOfferRepository, ProductRepository productRepository) {
        this.productOfferRepository = productOfferRepository;
        this.productRepository = productRepository;
    }


    @Override
    public String productOffers(Model model) {

        List<ProductOffers> offersList = productOfferRepository.findAll();
        model.addAttribute("productOffers",offersList);

        return "Admin/productOff";
    }

    @Override
    public String addProductOffer(ProductOfferDTO productOffer) {

        try{
            ProductOffers productOffers = new ProductOffers();
            Products products = productRepository.findAllById(productOffer.getProductId());
            double price = (productOffers.getOfferPercentage() / products.getPrice()) * 100;

            productOffers.setProducts(products);
            productOffers.setOfferPercentage(productOffer.getOfferPercentage());
            productOffers.setDiscountAmount(price);
            productOffers.setExpiryDate(productOffer.getExpiryDate());

            saveOffer(productOffers);
            return "redirect:/admin/product-off";

        }catch (Exception e){
            log.info("addProductOffer "+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public ProductOffers saveOffer(ProductOffers productOffers) {
        return productOfferRepository.save(productOffers);
    }


    @Override
    public String editProductOff(ProductOfferDTO productOfferDTO) {

        try {
            ProductOffers productOffers = productOfferRepository.findById(productOfferDTO.getOfferId()).get();
            double price = (productOffers.getOfferPercentage() / 100) * productOffers.getProducts().getPrice();

            productOffers.setOfferPercentage(productOfferDTO.getOfferPercentage());
            productOffers.setDiscountAmount(price);
            productOffers.setExpiryDate(productOfferDTO.getExpiryDate());

            saveOffer(productOffers);
            return "redirect:/admin/product-off";
        }catch (Exception e){
            log.info("editProductOff "+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    @Transactional
    public String removeProductOffer(Integer offerId) {

        try {
            productOfferRepository.deleteById(offerId);

            return "redirect:/admin/product-off";
        }catch (Exception c){

            return "Exception/404";
        }

    }
}
