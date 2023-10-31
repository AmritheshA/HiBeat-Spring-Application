package com.Hibeat.Hibeat.Servicess.User_Service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.TwiMLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioService {

    public void twilioSMS(String productName) {

        try {
            Twilio.init("ACe6ff540bd858b7b939b7a21a0375da53", "8231950341f0fbd172d6a3a5615914e1");
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+919961811304"),
                            new com.twilio.type.PhoneNumber("+12563443253"),
                            productName+" Have Reached The Maximum Stock Limit.....")
                    .create();
        }catch (Exception e){

            log.info("Some error occurred when trying to send SMS ");
            throw new TwiMLException("Error at TwilioService");
        }
    }
}
