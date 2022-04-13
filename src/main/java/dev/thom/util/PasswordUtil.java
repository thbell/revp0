package dev.thom.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordUtil {

    public static String hashString(String input) {
        String hashedString = "";

        byte[] salt = "lEH#$%,/^-=ieruj459DG068h()-=nljng#$%rgoFEejk".getBytes();
        char[] inputChars = input.toCharArray();
        int iterations = 65536;
        int keyLength = 512;

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(inputChars, salt, iterations, keyLength);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            byte[] hashedBytes = secretKey.getEncoded();

            hashedString = Hex.encodeHexString(hashedBytes);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }


        return hashedString;
    }

    public static String hideString(String input, int repeatCount) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < repeatCount; i++) {
            builder.append(input);
        }

        return builder.toString();
    }

    public static boolean hasValue(String input) {
        return input != null && input.trim() != "";
    }
}
