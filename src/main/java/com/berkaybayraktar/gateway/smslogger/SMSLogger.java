package com.berkaybayraktar.gateway.smslogger;

import com.berkaybayraktar.gateway.SMSService;
import org.jboss.logging.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SMSLogger implements SMSService {
    private static Logger log = Logger.getLogger(SMSLogger.class);

    @Override
    public boolean send(String phoneNumber, String message, String login, String pw) {

        String smsCodeString = "Phone Number: '" + phoneNumber + "', Message : '" + message + "', login: '" + login + "', password: '" + pw + "'";

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter("sms-codes.log", true));
            bw.write(smsCodeString);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
                // just ignore it
            }
        }

        return true;
    }

}
