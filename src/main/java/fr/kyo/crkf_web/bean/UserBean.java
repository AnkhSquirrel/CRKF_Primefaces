package fr.kyo.crkf_web.bean;

import fr.kyo.crkf_web.security.SecurityTools;
import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Compte;

import jakarta.faces.context.FacesContext;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;


@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private BeanManager beanManager;
    private Pbkdf2PasswordHashImpl pbkdf2PasswordHash;
    private Compte compte;
    private boolean formStatut;

    @PostConstruct
    private void init(){
        compte = new Compte();
        pbkdf2PasswordHash = new Pbkdf2PasswordHashImpl();
    }

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

    private void hash() {
        pbkdf2PasswordHash = new Pbkdf2PasswordHashImpl();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
        parameters.put("Pbkdf2PasswordHash.Iterations", "300000");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        parameters.put("Pbkdf2PasswordHash.KeySizeBytes", "64");

        pbkdf2PasswordHash.initialize(parameters);

        String generate = pbkdf2PasswordHash.generate(compte.getMot_de_passe().toCharArray());

        compte.setMot_de_passe(generate.split(":")[2] + ":" + generate.split(":")[3]);
    }

    public void login(){
        System.out.println("login");
    }

    public void changeForm(){
        formStatut = !formStatut;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public boolean isFormStatut() {
        return formStatut;
    }

    public void setFormStatut(boolean formStatut) {
        this.formStatut = formStatut;
    }
}
