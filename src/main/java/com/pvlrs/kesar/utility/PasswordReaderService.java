package com.pvlrs.kesar.utility;

import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class PasswordReaderService {

    private final PasswordReader passwordReader;

    public PasswordReaderService(PasswordReader passwordReader) {
        this.passwordReader = passwordReader;
    }

    public String parsePasswordIfAbsentOrElseGet(char[] passwordCharacters) {
        return isNull(passwordCharacters) ?
                passwordReader.readPassword() : new String(passwordCharacters);
    }
}
