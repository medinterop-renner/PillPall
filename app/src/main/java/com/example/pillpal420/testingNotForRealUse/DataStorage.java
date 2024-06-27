package com.example.pillpal420.testingNotForRealUse;

import java.util.concurrent.CountDownLatch;

public class DataStorage {
    private static int patientAnzahlAmServer = 5;
    static String[] patientenLocalStorage = new String[patientAnzahlAmServer -1];
    static RequestHandler requestHandler = new RequestHandler();

    public static void getAllPatientsFromServer(){

        for(int i = 1; i<patientAnzahlAmServer; i++) {
           patientenLocalStorage[i-1] =  requestHandler.makeRequest("http://10.0.2.2:8080/hapi-fhir-jpaserver/fhir/Patient/" + i);

        }
    }

    public static String[] getPatientData(){

        return patientenLocalStorage;
    }
}
