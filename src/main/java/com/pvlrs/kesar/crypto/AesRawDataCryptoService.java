package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.enums.CryptoServiceQualifier;
import com.pvlrs.kesar.exception.AesDecryptionException;
import com.pvlrs.kesar.exception.AesEncryptionException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;

import static com.pvlrs.kesar.constant.CryptoConstants.GCM_IV_SIZE_IN_BYTES;
import static com.pvlrs.kesar.constant.ErrorMessages.DECRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.ErrorMessages.ENCRYPTION_ERROR_MESSAGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.isNull;

@Service
public class AesRawDataCryptoService implements PasswordBasedCryptoService {

    private final CipherProvider cipherProvider;

    public AesRawDataCryptoService(CipherProvider cipherProvider) {
        this.cipherProvider = cipherProvider;
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
            byte[] iv = CryptoUtilities.generateInitializationVector();
            Cipher cipher = cipherProvider.getCipher(Cipher.ENCRYPT_MODE, iv, password);

            byte[] encryptedBytes = cipher.doFinal(plainTextInput.getBytes());
            byte[] result = new byte[GCM_IV_SIZE_IN_BYTES + encryptedBytes.length];

            System.arraycopy(iv, 0, result, 0, GCM_IV_SIZE_IN_BYTES);
            System.arraycopy(encryptedBytes, 0, result, GCM_IV_SIZE_IN_BYTES, encryptedBytes.length);

            return CryptoUtilities.base64Encode(result);
        } catch (Exception exception) {
            throw new AesEncryptionException(ENCRYPTION_ERROR_MESSAGE);
        }
    }

    public String decrypt(String encryptedInput, String password) {
        if (isNull(encryptedInput)) {
            return null;
        }

        try {
            byte[] ivAndEncryptedData = CryptoUtilities.base64Decode(encryptedInput);
            byte[] iv = copyOfRange(ivAndEncryptedData, 0, GCM_IV_SIZE_IN_BYTES);
            byte[] encryptedData = copyOfRange(ivAndEncryptedData, GCM_IV_SIZE_IN_BYTES, ivAndEncryptedData.length);
            Cipher cipher = cipherProvider.getCipher(Cipher.DECRYPT_MODE, iv, password);

            byte[] decrypted = cipher.doFinal(encryptedData);
            return new String(decrypted, UTF_8);
        } catch (Exception exception) {
            throw new AesDecryptionException(DECRYPTION_ERROR_MESSAGE);
        }
    }
}
