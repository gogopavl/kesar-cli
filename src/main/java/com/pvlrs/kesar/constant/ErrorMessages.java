package com.pvlrs.kesar.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {

    public static final String ENCRYPTION_ERROR_MESSAGE = "Encountered an exception while trying to encrypt your " +
            "data. Are you sure you typed in the correct password?";
    public static final String DECRYPTION_ERROR_MESSAGE = "Encountered an exception while trying to decrypt your " +
            "data. Are you sure you typed in the correct password?";
    public static final String FILE_ENCRYPTION_ERROR_MESSAGE = "Encountered an exception while trying to encrypt " +
            "your file. Are you sure you typed in the correct password? Is the path provided correct?";
    public static final String FILE_DECRYPTION_ERROR_MESSAGE = "Encountered an exception while trying to decrypt " +
            "your file. Are you sure you typed in the correct password? Is the path provided correct?";
}
