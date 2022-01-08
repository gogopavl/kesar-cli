package com.pvlrs.kesar.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CryptoConstants {

    public static final String AES = "AES";
    public static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    public static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int AES_KEY_SIZE_IN_BITS = 256;
    public static final int GCM_TAG_SIZE_IN_BITS = 128;
    public static final int GCM_IV_SIZE_IN_BYTES = 12;
    public static final int PBE_ITERATION_COUNT = 65536;
}
