package com.Hibeat.Hibeat.Controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.TwiMLException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/TwilioSample")
    public ResponseEntity<String> sampleTwilio() {

        try {
            Twilio.init("ACe6ff540bd858b7b939b7a21a0375da53", "8231950341f0fbd172d6a3a5615914e1");
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+919961811304"),
                            new com.twilio.type.PhoneNumber("+12563443253"),
                            "Sample Message From Twilio")
                    .create();
        }catch (Exception e){
            throw new TwiMLException("Error");
        }

        return ResponseEntity.ok().body("Success");
    }
}
