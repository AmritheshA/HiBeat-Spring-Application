package com.Hibeat.Hibeat.Servicess.User_Service;


import java.io.IOException;

public interface InvoiceService {
    byte[] createInvoice(Integer orderId) throws IOException;

}
