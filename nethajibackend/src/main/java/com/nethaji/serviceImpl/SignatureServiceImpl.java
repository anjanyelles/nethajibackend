package com.nethaji.serviceImpl;



import com.nethaji.service.SignatureService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class SignatureServiceImpl implements SignatureService {

    private static final int NO_OF_UPDTES = 100;

    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();


    @Override
    public String hashPassword(String password, String salt) throws RuntimeException {
        String toBeHashed = password + salt;
        try {

            byte[] stringToHash = toBeHashed.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());

            for (int i = 0; i < NO_OF_UPDTES; i++) {
                md.reset();
                stringToHash = md.digest(stringToHash);
            }

            return bytesToHex(stringToHash);


        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}

