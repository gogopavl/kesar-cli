package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.configuration.CryptoConfiguration;
import com.pvlrs.kesar.enums.CryptoServiceQualifier;
import com.pvlrs.kesar.exception.AesDecryptionException;
import com.pvlrs.kesar.exception.AesEncryptionException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static com.pvlrs.kesar.constant.CryptoConstants.AES;
import static com.pvlrs.kesar.constant.CryptoConstants.AES_KEY_SIZE_IN_BITS;
import static com.pvlrs.kesar.constant.CryptoConstants.AES_TRANSFORMATION;
import static com.pvlrs.kesar.constant.CryptoConstants.GCM_IV_SIZE_IN_BYTES;
import static com.pvlrs.kesar.constant.CryptoConstants.GCM_TAG_SIZE_IN_BITS;
import static com.pvlrs.kesar.constant.CryptoConstants.KEY_DERIVATION_ALGORITHM;
import static com.pvlrs.kesar.constant.CryptoConstants.PBE_ITERATION_COUNT;
import static com.pvlrs.kesar.constant.ErrorMessages.DECRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.ErrorMessages.ENCRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.FileConstants.BUFFER_SIZE;
import static com.pvlrs.kesar.constant.FileConstants.END_OF_FILE_CODE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

// todo: refactor most of the stuff in here
@Service
public class AesFileCryptoService implements PasswordBasedCryptoService {

    private final CryptoConfiguration cryptoConfiguration;

    public AesFileCryptoService(CryptoConfiguration cryptoConfiguration) {
        this.cryptoConfiguration = cryptoConfiguration;
    }

    @Override
    public CryptoServiceQualifier getQualifier() {
        return CryptoServiceQualifier.AES_FILE_CRYPTO_SERVICE;
    }

    public String encrypt(String filePath, String password) {
        if (isNull(filePath)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKey secretKey = deriveKeyFromPassword(password);
            byte[] iv = generateInitializationVector();
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_SIZE_IN_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            String outputFileName = filePath + ".enc";
            byte[] buffer = new byte[BUFFER_SIZE];

            try (FileInputStream inputStream = new FileInputStream(filePath);
                 FileOutputStream outputStream = new FileOutputStream(outputFileName)) {

                outputStream.write(iv);
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != END_OF_FILE_CODE) {
                    byte[] output = cipher.update(buffer, 0, bytesRead);
                    if (nonNull(output)) {
                        outputStream.write(output);
                    }
                }
                byte[] outputBytes = cipher.doFinal();
                if (nonNull(outputBytes)) {
                    outputStream.write(outputBytes);
                }
            }

            return outputFileName;
        } catch (Exception exception) {
            // todo: don't catch generic exception, but specific sub-types & provide appropriate messages & error codes
            throw new AesEncryptionException(ENCRYPTION_ERROR_MESSAGE);
        }
    }

    private SecretKey deriveKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = cryptoConfiguration.getSalt();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                PBE_ITERATION_COUNT, AES_KEY_SIZE_IN_BITS);
        byte[] encodedKey = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(encodedKey, AES);
    }

    private static byte[] generateInitializationVector() {
        byte[] iv = new byte[GCM_IV_SIZE_IN_BYTES];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    public String decrypt(String encryptedFilePath, String password) {
        if (isNull(encryptedFilePath)) {
            return null;
        }

        try {
            String outputFileName = encryptedFilePath + ".dec";

            try (FileInputStream fileInputStream = new FileInputStream(encryptedFilePath);
                 FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
                byte[] fileIv = new byte[GCM_IV_SIZE_IN_BYTES];
                fileInputStream.read(fileIv);

                Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
                SecretKey secretKey = deriveKeyFromPassword(password);
                GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_SIZE_IN_BITS, fileIv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != END_OF_FILE_CODE) {
                    byte[] output = cipher.update(buffer, 0, bytesRead);
                    if (nonNull(output)) {
                        fileOutputStream.write(output);
                    }
                }
                byte[] output = cipher.doFinal();
                if (nonNull(output)) {
                    fileOutputStream.write(output);
                }
            }

            return outputFileName;
        } catch (Exception exception) {
            // todo: don't catch generic exception, but specific sub-types & provide appropriate messages & error codes
            throw new AesDecryptionException(DECRYPTION_ERROR_MESSAGE);
        }
    }
}
