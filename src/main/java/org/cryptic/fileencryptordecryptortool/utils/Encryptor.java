package org.cryptic.fileencryptordecryptortool.utils;

import jakarta.xml.bind.DatatypeConverter;
import jakarta.xml.bind.JAXBException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Encryptor {

    // AES Configuration
    private static final byte[] ivBytesAES = { (byte) 0xA1, (byte) 0xB2, (byte) 0xC3, (byte) 0xD4,
            (byte) 0xE5, (byte) 0xF6, (byte) 0x07, (byte) 0x18, (byte) 0x29, (byte) 0x3A,
            (byte) 0x4B, (byte) 0x5C, (byte) 0x6D, (byte) 0x7E, (byte) 0x8F, (byte) 0x90 };
    private static final byte[] keyBytesAES = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
            (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0, (byte) 0x12, (byte) 0x34,
            (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
    private static final String ENCRYPTION_TRANSFORMATION_AES = "AES/CBC/PKCS5Padding";

    // DES Configuration
    private static final byte[] ivBytesDES = { (byte) 0xD2, (byte) 0x36, (byte) 0xC9, (byte) 0x3F,
            (byte) 0xED, (byte) 0x8E, (byte) 0x2F, (byte) 0x90 };
    private static final byte[] keyBytesDES = { (byte) 0xF4, (byte) 0xC2, (byte) 0xB3, (byte) 0x4A,
            (byte) 0xA8, (byte) 0x7A, (byte) 0x13, (byte) 0x23 };
    private static final String ENCRYPTION_TRANSFORMATION_DES = "DES/CBC/PKCS5Padding";

    // GCM Configuration
    private static final byte[] ivBytesGCM = { (byte) 0xD3, (byte) 0xE4, (byte) 0xF5, (byte) 0x06,
            (byte) 0x17, (byte) 0x28, (byte) 0x39, (byte) 0x4A, (byte) 0x5B, (byte) 0x6C,
            (byte) 0x7D, (byte) 0x8E };
    private static final String ENCRYPTION_TRANSFORMATION_GCM = "AES/GCM/NoPadding";
    private static final String ENCRYPTION_ALGORITHM_AES = "AES";
    private static final String ENCRYPTION_ALGORITHM_DES = "DES";

    private Encryptor() {
        // Utility class, no instantiation
    }


    public static String encrypt(String inputString, String algorithm) throws JAXBException {
        try {
            Cipher cipher = getCipher(true, algorithm);
            byte[] encryptedBytes = cipher.doFinal(inputString.getBytes());
            return DatatypeConverter.printBase64Binary(encryptedBytes);
        } catch (Exception ex) {
            throw new JAXBException(ex);
        }
    }


    public static String decrypt(String encryptedString, String algorithm) throws JAXBException {
        try {
            Cipher cipher = getCipher(false, algorithm);
            byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encryptedString);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new JAXBException(ex);
        }
    }

    private static Cipher getCipher(boolean encrypt, String algorithm) throws JAXBException {
        try {
            Cipher cipher;
            int mode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

            switch (algorithm.toUpperCase()) {
                case "GCM":
                    cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION_GCM);
                    GCMParameterSpec gcmSpec = new GCMParameterSpec(128, ivBytesGCM); // 128-bit auth tag
                    SecretKey gcmKey = new SecretKeySpec(keyBytesAES, ENCRYPTION_ALGORITHM_AES);
                    cipher.init(mode, gcmKey, gcmSpec);
                    break;

                case "DES":
                    cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION_DES);
                    IvParameterSpec ivSpecDES = new IvParameterSpec(ivBytesDES);
                    SecretKey desKey = new SecretKeySpec(keyBytesDES, ENCRYPTION_ALGORITHM_DES);
                    cipher.init(mode, desKey, ivSpecDES);
                    break;

                case "AES":
                default:
                    cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION_AES);
                    IvParameterSpec ivSpecAES = new IvParameterSpec(ivBytesAES);
                    SecretKey aesKey = new SecretKeySpec(keyBytesAES, ENCRYPTION_ALGORITHM_AES);
                    cipher.init(mode, aesKey, ivSpecAES);
                    break;
            }

            return cipher;
        } catch (Exception ex) {
            throw new JAXBException(ex);
        }
    }
}
