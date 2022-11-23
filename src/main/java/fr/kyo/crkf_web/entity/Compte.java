package fr.kyo.crkf_web.entity;

public class Compte {
    private String email;
    private String password;

    public Compte() {
        this.email = "";
        this.password = "";
    }

    public Compte(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
