package com.pvlrs.kesar.exception;

import static com.pvlrs.kesar.constant.KesarCliExitCodes.DECRYPTION_ERROR_CODE;

public class AesDecryptionException extends AbstractExitCodeException {

    public AesDecryptionException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return DECRYPTION_ERROR_CODE;
    }
}
