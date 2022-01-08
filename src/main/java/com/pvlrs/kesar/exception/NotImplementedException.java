package com.pvlrs.kesar.exception;

import static com.pvlrs.kesar.constant.KesarCliExitCodes.NOT_IMPLEMENTED_ERROR_CODE;

public class NotImplementedException extends AbstractExitCodeException {

    public NotImplementedException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return NOT_IMPLEMENTED_ERROR_CODE;
    }
}
