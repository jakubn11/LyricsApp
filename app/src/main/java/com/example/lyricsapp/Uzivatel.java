package com.example.lyricsapp;

public class Uzivatel {
    private String prezdivka;
    private String email;
    private String heslo;
    private String profilovka;

    public Uzivatel(String prezdivka, String email, String heslo, String profilovka) {
        this.prezdivka = prezdivka;
        this.email = email;
        this.heslo = heslo;
        this.profilovka = profilovka;
    }

    public Uzivatel() {
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

    public String getProfilovka() {
        return profilovka;
    }

    public void setProfilovka(String profilovka) {
        this.profilovka = profilovka;
    }
}
