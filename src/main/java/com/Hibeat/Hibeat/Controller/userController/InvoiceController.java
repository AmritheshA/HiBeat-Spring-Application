package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Servicess.User_Service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class InvoiceController {


    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateInvoice(@RequestParam("id") Integer orderId) {
        try {

            log.info("orderId"+orderId);

            byte[] invoiceBytes = invoiceService.createInvoice(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "invoice.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
