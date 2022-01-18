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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import static com.pvlrs.kesar.constant.CryptoConstants.AES;
import static com.pvlrs.kesar.constant.CryptoConstants.AES_KEY_SIZE_IN_BITS;
import static com.pvlrs.kesar.constant.CryptoConstants.AES_TRANSFORMATION;
import static com.pvlrs.kesar.constant.CryptoConstants.GCM_IV_SIZE_IN_BYTES;
import static com.pvlrs.kesar.constant.CryptoConstants.GCM_TAG_SIZE_IN_BITS;
import static com.pvlrs.kesar.constant.CryptoConstants.KEY_DERIVATION_ALGORITHM;
import static com.pvlrs.kesar.constant.CryptoConstants.PBE_ITERATION_COUNT;
import static com.pvlrs.kesar.constant.ErrorMessages.DECRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.ErrorMessages.ENCRYPTION_ERROR_MESSAGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.isNull;

// todo: refactor most of the stuff in here
@Service
public class AesRawDataCryptoService implements PasswordBasedCryptoService {

    private final CryptoConfiguration cryptoConfiguration;

    public AesRawDataCryptoService(CryptoConfiguration cryptoConfiguration) {
        this.cryptoConfiguration = cryptoConfiguration;
    }

    @Override
    public CryptoServiceQualifier getQualifier() {
        return CryptoServiceQualifier.AES_RAW_DATA_CRYPTO_SERVICE;
    }

    public String encrypt(String plainTextInput, String password) {
        if (isNull(plainTextInput)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKey secretKey = deriveKeyFromPassword(password);
            byte[] iv = generateInitializationVector();
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_SIZE_IN_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encryptedBytes = cipher.doFinal(plainTextInput.getBytes());
            byte[] result = new byte[GCM_IV_SIZE_IN_BYTES + encryptedBytes.length];

            System.arraycopy(iv, 0, result, 0, GCM_IV_SIZE_IN_BYTES);
            System.arraycopy(encryptedBytes, 0, result, GCM_IV_SIZE_IN_BYTES, encryptedBytes.length);

            return base64Encode(result);
        } catch (Exception exception) {
            // todo: don't catch generic exception, but specific sub-types & provide appropriate messages & error codes
            throw new AesEncryptionException(ENCRYPTION_ERROR_MESSAGE);
        }
    }

    // todo: refactor
    private SecretKey deriveKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = cryptoConfiguration.getSalt();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                PBE_ITERATION_COUNT, AES_KEY_SIZE_IN_BITS);
        byte[] encodedKey = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(encodedKey, AES);
    }

    // todo: refactor
    private static byte[] generateInitializationVector() {
        byte[] iv = new byte[GCM_IV_SIZE_IN_BYTES];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    private static String base64Encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public String decrypt(String encryptedInput, String password) {
        if (isNull(encryptedInput)) {
            return null;
        }

        try {
            byte[] ivAndEncryptedData = base64Decode(encryptedInput);
            byte[] iv = copyOfRange(ivAndEncryptedData, 0, GCM_IV_SIZE_IN_BYTES);
            byte[] encryptedData = copyOfRange(ivAndEncryptedData, GCM_IV_SIZE_IN_BYTES, ivAndEncryptedData.length);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKey secretKey = deriveKeyFromPassword(password);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_SIZE_IN_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decrypted = cipher.doFinal(encryptedData);

            return new String(decrypted, UTF_8);
        } catch (Exception exception) {
            // todo: don't catch generic exception, but specific sub-types & provide appropriate messages & error codes
            throw new AesDecryptionException(DECRYPTION_ERROR_MESSAGE);
        }
    }

    private static byte[] base64Decode(String input) {
        return Base64.getDecoder().decode(input.getBytes(UTF_8));
    }
}
