package fr.kyo.crkf_web.bean;

import fr.kyo.crkf_web.security.SecurityTools;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    private transient String email;
    private transient String password;

    public void sendVerificationEmail() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String verificationUrl = SecurityTools.generateVerificationUrl(email, password);
        String body = "Lien de verif = " + verificationUrl;
        //Email.sendEmail(email, "Verification d'email", body);
        System.out.println(body);
    }

    public String verifyUrl() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String verificationCode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");

        String checksum = SecurityTools.checksum(email+password).toString();
        String encryptedData = SecurityTools.encrypt(email + ":" + password + ":" + checksum);


        if (verificationCode.equals(encryptedData) /* && le lien n'est pas expiré && l'user n'existe pas deja */){
            // Création du compte
            return "correct, création du comtpe";
        } else {
            return "incorrect ou expiré, rien ne se passe";
        }
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
