package com.pvlrs.kesar.exception;

import static com.pvlrs.kesar.constant.KesarCliExitCodes.ENCRYPTION_ERROR_CODE;

public class AesEncryptionException extends AbstractExitCodeException {

    public AesEncryptionException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return ENCRYPTION_ERROR_CODE;
    }
}
