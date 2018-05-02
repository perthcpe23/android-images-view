package com.longdo.android.api;

/**
 * Created by perth on 15/1/2018 AD.
 */

public class ImageData {

    public Object image;
    public String title;
    public String owner;
    public String date;

    public ImageData(Object image, String title, String owner, String date){
        this.image = image;
        this.title = title;
        this.owner = owner;
        this.date = date;
    }

}
