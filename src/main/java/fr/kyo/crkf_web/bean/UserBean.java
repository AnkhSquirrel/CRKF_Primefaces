package fr.kyo.crkf_web.bean;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.security.Email;
import fr.kyo.crkf_web.security.SecurityTools;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

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

    public void sendVerificationEmail() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        hash();
        String verificationUrl = SecurityTools.generateVerificationUrl(compte.getEmail(), compte.getPassword());
        String genericBody = getResourceFileAsString("/mails/verification.html");
        String customizedBody = genericBody.replaceAll("VERIFICATION_URL", verificationUrl);
        System.out.println(customizedBody);
        //Email.sendEmail(compte.getEmail(), "Vérification d'email", customizedBody);
    }

    private String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream inputStream = Objects.requireNonNull(getClass().getResource(fileName)).openStream()){
            if (inputStream == null) return null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public String verifyUrl() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptedVerificationCode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        String decryptedVerificationCode = SecurityTools.decrypt(encryptedVerificationCode);
        String[] verificationCodeVars = decryptedVerificationCode.split(":");

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        String verificationCodeChecksum = SecurityTools.checksum(verificationCodeVars[0] + verificationCodeVars[1] + ":" + verificationCodeVars[2] + verificationCodeVars[3]).toString();

        boolean verificationCodeIsExpired = calendar.getTimeInMillis() > Long.parseLong(verificationCodeVars[3]);
        boolean emailIsUsed = DAOFactory.getCompteDAO().exists(verificationCodeVars[0]);
        boolean checksumIsInvalid = !verificationCodeChecksum.equals(verificationCodeVars[4]);

        if (verificationCodeIsExpired || emailIsUsed || checksumIsInvalid){
            return "incorrect ou expiré, rien ne se passe";
        } else {
            compte.setEmail(verificationCodeVars[0]);
            compte.setPassword(verificationCodeVars[1] + ":" + verificationCodeVars[2]);
            DAOFactory.getCompteDAO().insert(compte);
            return "correct, création du comtpe";
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

        String generate = pbkdf2PasswordHash.generate(compte.getPassword().toCharArray());

        compte.setPassword(generate.split(":")[2] + ":" + generate.split(":")[3]);
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
