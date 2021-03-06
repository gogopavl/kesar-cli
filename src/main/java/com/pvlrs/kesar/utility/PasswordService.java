package com.pvlrs.kesar.utility;

import com.pvlrs.kesar.configuration.CryptoConfiguration;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static com.pvlrs.kesar.constant.CryptoConstants.AES;
import static com.pvlrs.kesar.constant.CryptoConstants.AES_KEY_SIZE_IN_BITS;
import static com.pvlrs.kesar.constant.CryptoConstants.KEY_DERIVATION_ALGORITHM;
import static com.pvlrs.kesar.constant.CryptoConstants.PBE_ITERATION_COUNT;
import static com.pvlrs.kesar.constant.KesarCliConstants.KESAR_CLI_ENV_PASSWORD;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class PasswordService {

    private final PasswordReader passwordReader;
    private final CryptoConfiguration cryptoConfiguration;

    public PasswordService(PasswordReader passwordReader, CryptoConfiguration cryptoConfiguration) {
        this.passwordReader = passwordReader;
        this.cryptoConfiguration = cryptoConfiguration;
    }

    public String parsePasswordIfAbsentOrElseGet(char[] passwordCharacters) {
        return isNull(passwordCharacters) ?
                getPassword() : new String(passwordCharacters);
    }

    private String getPassword() {
        String environmentPassword = System.getenv(KESAR_CLI_ENV_PASSWORD);
        if (isNotBlank(environmentPassword)) {
            return environmentPassword;
        } else {
            return passwordReader.readPassword();
        }
    }

    public SecretKey deriveKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = cryptoConfiguration.getSalt();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                PBE_ITERATION_COUNT, AES_KEY_SIZE_IN_BITS);
        byte[] encodedKey = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(encodedKey, AES);
    }
}
