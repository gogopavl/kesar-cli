package com.pvlrs.kesar.crypto;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;

import static com.pvlrs.kesar.constant.CryptoConstants.GCM_IV_SIZE_IN_BYTES;
import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class CryptoUtilities {

    public static byte[] generateInitializationVector() {
        byte[] iv = new byte[GCM_IV_SIZE_IN_BYTES];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static String base64Encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static byte[] base64Decode(String input) {
        return Base64.getDecoder().decode(input.getBytes(UTF_8));
    }
}
