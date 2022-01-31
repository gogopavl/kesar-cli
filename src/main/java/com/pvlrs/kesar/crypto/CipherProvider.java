package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.utility.PasswordService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.pvlrs.kesar.constant.CryptoConstants.AES_TRANSFORMATION;
import static com.pvlrs.kesar.constant.CryptoConstants.GCM_TAG_SIZE_IN_BITS;

@Service
public class CipherProvider {

    private final PasswordService passwordService;

    public CipherProvider(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public Cipher getCipher(int mode,
                            byte[] iv,
                            String password)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        SecretKey secretKey = passwordService.deriveKeyFromPassword(password);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_SIZE_IN_BITS, iv);
        cipher.init(mode, secretKey, spec);
        return cipher;
    }
}
