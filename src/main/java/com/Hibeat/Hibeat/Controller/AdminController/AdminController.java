package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminDashboardService;
import com.Hibeat.Hibeat.Servicess.Admin_Service.SalesReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final AdminDashboardService adminDashboardService;
    private final SalesReportService salesReportService;

    @Autowired
    public AdminController(AdminDashboardService adminDashboardService, SalesReportService salesReportService) {
        this.adminDashboardService = adminDashboardService;
        this.salesReportService = salesReportService;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return adminDashboardService.dashboard(model);
    }


    @GetMapping("/date-wise/download")
    public ResponseEntity<byte[]> salesReport(@RequestParam("startDate")String startDate,
                                                        @RequestParam("endDate") String endDate){
        try {

            byte[] invoiceBytes = salesReportService.salesReport(startDate,endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/monthly-sales-report")
    public ResponseEntity<byte[]> monthlySalesReport(){
        try {

            byte[] invoiceBytes = salesReportService.monthlySalesReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "monthly-sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/yearly-sales-report")
    public ResponseEntity<byte[]> yearlySalesReport(){
        try {

            byte[] invoiceBytes = salesReportService.yearlySalesReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "yearly-sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
