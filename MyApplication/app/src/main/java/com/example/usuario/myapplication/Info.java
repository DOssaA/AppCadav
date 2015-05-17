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
    String usuario1;
    String usuario2;
    Bitmap bitmap1;
    Bitmap bitmap2;

    public Info(String url, String url1, String title, String description, String usuario1, String usuario2, Bitmap bitmap1, Bitmap bitmap2) {
        this.url = url;
        this.url1 = url1;
        this.title = title;
        this.description = description;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.bitmap1 = bitmap1;
        this.bitmap2 = bitmap2;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public Info() {
        this.url = null;
        this.url1 = null;
        this.title = null;
        this.description = null;
        this.usuario1 = null;
        this.usuario2 = null;

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
