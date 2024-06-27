package com.example.pillpal420;

import android.graphics.Bitmap;

public class InvItem {
    private String picPath;
    private String picName;
    private String expiryDate;
    private Bitmap picBitmap;

    public InvItem(String picPath, String picName, String expiryDate){

        this.picName = picName;
        this.picPath = picPath;
        this.expiryDate = expiryDate;
    }

    public String getPicPath(){return picPath;}
    public String getPicName(){return picName;}
    public String getExpiryDate(){return expiryDate;}
    public Bitmap getPicBitmap(){return picBitmap;}


    public void setName(String name) {
        this.picName = name;
    }

    public void setDate(String date) {
        this.expiryDate = date;
    }
}
