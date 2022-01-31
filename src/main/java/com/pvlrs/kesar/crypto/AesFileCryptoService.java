package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.enums.CryptoServiceQualifier;
import com.pvlrs.kesar.exception.AesDecryptionException;
import com.pvlrs.kesar.exception.AesEncryptionException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.pvlrs.kesar.constant.CryptoConstants.GCM_IV_SIZE_IN_BYTES;
import static com.pvlrs.kesar.constant.ErrorMessages.FILE_DECRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.ErrorMessages.FILE_ENCRYPTION_ERROR_MESSAGE;
import static com.pvlrs.kesar.constant.FileConstants.BUFFER_SIZE;
import static com.pvlrs.kesar.constant.FileConstants.END_OF_FILE_CODE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class AesFileCryptoService implements PasswordBasedCryptoService {

    private final CipherProvider cipherProvider;

    public AesFileCryptoService(CipherProvider cipherProvider) {
        this.cipherProvider = cipherProvider;
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
            byte[] iv = CryptoUtilities.generateInitializationVector();
            Cipher cipher = cipherProvider.getCipher(Cipher.ENCRYPT_MODE, iv, password);
            return encryptFile(filePath, iv, cipher);
        } catch (Exception exception) {
            throw new AesEncryptionException(FILE_ENCRYPTION_ERROR_MESSAGE);
        }
    }

    private String encryptFile(String filePath,
                               byte[] iv,
                               Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {

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
    }

    public String decrypt(String encryptedFilePath, String password) {
        if (isNull(encryptedFilePath)) {
            return null;
        }

        try {
            String outputFileName = encryptedFilePath + ".dec";

            try (FileInputStream fileInputStream = new FileInputStream(encryptedFilePath);
                 FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {

                byte[] fileIv = readIv(fileInputStream);
                Cipher cipher = cipherProvider.getCipher(Cipher.DECRYPT_MODE, fileIv, password);
                decryptFile(fileInputStream, fileOutputStream, cipher);
            }
            return outputFileName;
        } catch (Exception exception) {
            throw new AesDecryptionException(FILE_DECRYPTION_ERROR_MESSAGE);
        }
    }

    private byte[] readIv(FileInputStream fileInputStream) throws IOException {
        byte[] fileIv = new byte[GCM_IV_SIZE_IN_BYTES];
        fileInputStream.read(fileIv);
        return fileIv;
    }

    private void decryptFile(FileInputStream fileInputStream,
                             FileOutputStream fileOutputStream,
                             Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {
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
}
