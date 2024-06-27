package com.example.pillpal420.testingNotForRealUse;

import java.util.concurrent.CountDownLatch;

public class RequestHandler {


    Requests requests = new Requests();



           public String makeRequest(String url){
               final String[] responese = new String[1];
               requests.getRequest(url, new Requests.FHIRRequestCallback() {
                   @Override
                   public void onResponseReceived(String responseBody) {
                        responese[0] = responseBody;


                   }

                   @Override
                   public void onFailure(Exception e) {
                       e.printStackTrace();

                   }
               });
               return responese[0];
           }
}
