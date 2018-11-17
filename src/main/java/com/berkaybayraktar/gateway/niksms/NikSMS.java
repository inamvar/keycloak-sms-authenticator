package com.berkaybayraktar.gateway.niksms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.berkaybayraktar.gateway.SMSService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.logging.Logger;

public class NikSMS implements SMSService {
    private static Logger log = Logger.getLogger(NikSMS.class);

    @Override
    public boolean send(String phoneNumber, String message, String username, String pw, String lineNumber) {
        try {
            String smsCodeString = "Phone Number: '" + phoneNumber + "', Message : '" + message + "', username: '" + username
                    + "', password: '" + pw + "'";
            log.info(smsCodeString);
            String url = "https://niksms.com/fa/publicapi/groupsms";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            // add header
            // post.setHeader("User-Agent", USER_AGENT);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("username",username));
            urlParameters.add(new BasicNameValuePair("password", pw));
            urlParameters.add(new BasicNameValuePair("sendType", "1"));
            urlParameters.add(new BasicNameValuePair("numbers", phoneNumber));
            urlParameters.add(new BasicNameValuePair("senderNumber", lineNumber));
            urlParameters.add(new BasicNameValuePair("message", message));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            log.info("\nSending 'POST' request to URL : " + url);
            log.info("Post parameters : " + post.getEntity());
            log.info("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            log.info(result.toString());
            return true;

        } catch (MalformedURLException e) {
          
            e.printStackTrace();
            return false;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }


    }

}
