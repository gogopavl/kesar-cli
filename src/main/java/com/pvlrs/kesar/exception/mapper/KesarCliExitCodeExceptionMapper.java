package com.pvlrs.kesar.exception.mapper;

import com.pvlrs.kesar.constant.KesarCliExitCodes;
import com.pvlrs.kesar.exception.AbstractExitCodeException;
import picocli.CommandLine.IExitCodeExceptionMapper;

public class KesarCliExitCodeExceptionMapper implements IExitCodeExceptionMapper {

    @Override
    public int getExitCode(Throwable throwable) {

        if (throwable instanceof AbstractExitCodeException) {
            return ((AbstractExitCodeException) throwable).getExitCode();
        }
        return KesarCliExitCodes.GENERIC_ERROR_CODE;
    }
}
