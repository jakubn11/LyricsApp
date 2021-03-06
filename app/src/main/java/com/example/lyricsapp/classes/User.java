package com.example.lyricsapp.classes;

public class User {
    private int id;
    private String prezdivka;
    private String email;
    private String heslo;
    byte[] profilovka;

    public User(String prezdivka, String email, String heslo, byte[] profilovka) {
        this.prezdivka = prezdivka;
        this.email = email;
        this.heslo = heslo;
    }

    public User(String prezdivka, String email, String heslo) {
        this.prezdivka = prezdivka;
        this.email = email;
        this.heslo = heslo;
        this.prezdivka = prezdivka;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrezdivka() {
        return prezdivka;
    }

    public void setPrezdivka(String prezdivka) {
        this.prezdivka = prezdivka;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public byte[] getProfilovka() {
        return profilovka;
    }

    public void setProfilovka(byte[] profilovka) {
        this.profilovka = profilovka;
    }
}
