package com.example.usuario.myapplication;

import android.graphics.Bitmap;

/**
 * Clase que contiene la informacion de las creaciones
 */
public class Info {
    String url;
    String url1; //id imagen cadaver
    String title;    //titulo
    String description;     //descripción

    public Info(){
        this.url= null;
        this.title = null;
        this.description=null;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
