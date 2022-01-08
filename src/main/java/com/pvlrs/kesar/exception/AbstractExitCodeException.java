package com.pvlrs.kesar.exception;

import com.pvlrs.kesar.constant.KesarCliExitCodes;
import org.springframework.boot.ExitCodeGenerator;

public abstract class AbstractExitCodeException extends RuntimeException implements ExitCodeGenerator {

    protected AbstractExitCodeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return KesarCliExitCodes.GENERIC_ERROR_CODE;
    }
}