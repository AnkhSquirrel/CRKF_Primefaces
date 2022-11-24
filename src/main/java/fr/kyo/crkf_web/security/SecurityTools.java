package fr.kyo.crkf_web.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static String generateVerificationUrl(String email, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 15);
        long timeLimit = calendar.getTimeInMillis();
        String checksum = checksum(email + password + timeLimit).toString();
        String encryptedData = encrypt(email + ";" + password + ";" + timeLimit + ";" + checksum);
        return "localhost:8080/CRKF_Web_war_exploded/faces/verification.xhtml?code=" + encryptedData;
    }
}