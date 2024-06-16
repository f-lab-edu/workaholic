package com.project.workaholic.config.encoder;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordEncoder {
    public String encrypt(String id, String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), setSalt(id), 65536, 256);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = keyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] setSalt(String id) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] keyBytes = id.trim().getBytes();
            return messageDigest.digest(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
