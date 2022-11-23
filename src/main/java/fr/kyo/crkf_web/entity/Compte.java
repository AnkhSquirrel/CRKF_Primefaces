package fr.kyo.crkf_web.entity;

public class Compte {
    private String email;
    private String mot_de_passe;

    public Compte() {
        this.email = "";
        this.mot_de_passe = "";
    }

    public Compte(String email, String mot_de_passe) {
        this.email = email;
        this.mot_de_passe = mot_de_passe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
}
