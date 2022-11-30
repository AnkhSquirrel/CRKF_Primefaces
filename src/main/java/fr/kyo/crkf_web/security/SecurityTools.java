package fr.kyo.crkf_web.security;

import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SecurityTools {

    private SecurityTools() {
    }

    private static final String ALGORITHM = "AES"; // AES/GCM/NoPadding
    private static final SecretKeySpec secretKey = new SecretKeySpec("TheVerySecretK3Y".getBytes(), ALGORITHM);

    public static String encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return new String(Base64.getUrlEncoder().encode(encryptedData));
    }

    public static String decrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decryptedData = Base64.getUrlDecoder().decode(data);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(decryptedData));
    }

    public static Long checksum(String string) {
        long checksum = 0;
        for (int i = 0; i < string.length(); i++) {
            checksum += (long) i * string.charAt(i);
        }
        return checksum;
    }

    public static String hash(String stringToHash) {
        Pbkdf2PasswordHash pbkdf2PasswordHash = new Pbkdf2PasswordHashImpl();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
        parameters.put("Pbkdf2PasswordHash.Iterations", "300000");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        parameters.put("Pbkdf2PasswordHash.KeySizeBytes", "64");
        pbkdf2PasswordHash.initialize(parameters);
        return pbkdf2PasswordHash.generate(stringToHash.toCharArray());
    }

    public static String generateVerificationUrl(String email, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 15);
        long timeLimit = calendar.getTimeInMillis();
        String checksum = checksum(email + password + timeLimit).toString();
        String encryptedData = encrypt(email + ";" + password + ";" + timeLimit + ";" + checksum);
        return "http://localhost:8080/CRKF_Web_war_exploded/faces/verification.xhtml?code=" + encryptedData;
    }

    public static boolean checkVerificationCodeFormat(String encryptedVerificationCode) {
        try {
            String decryptedVerificationCode = decrypt(encryptedVerificationCode);
            String[] verificationCodeVars = decryptedVerificationCode.split(";");

            String password = verificationCodeVars[1];
            if (password.length() != 205) return false;

            String timeLimit = verificationCodeVars[2];
            if (!timeLimit.matches("^[0-9]*$") || timeLimit.length() > 18) return false;

            String checksum = verificationCodeVars[3];
            if (!checksum.matches("^[0-9]*$") || checksum.length() > 18) return false;
        } catch (IllegalArgumentException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException exception) {
            return false;
        }
        return true;
    }
}