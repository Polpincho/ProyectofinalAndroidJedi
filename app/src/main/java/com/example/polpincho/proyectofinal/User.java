package com.example.polpincho.proyectofinal;

/**
 * Created by polpincho on 01/02/2016.
 */
public class User {


    private String uri;
    private String username;
    private String mail;
    private int puntuation;

    User(String username, String mail, String uri,int puntuation){
        this.username = username;
        this.mail = mail;
        this.puntuation = puntuation;
        this.uri = uri;

    }
    User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPuntuation() {
        return puntuation;
    }

    public void setPuntuation(int puntuation) {
        this.puntuation = puntuation;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

