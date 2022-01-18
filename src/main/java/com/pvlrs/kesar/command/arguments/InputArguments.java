package com.pvlrs.kesar.command.arguments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
@Builder
@AllArgsConstructor
public class InputArguments {

    private String password;
    private String data;
    private String pathToFile;

    public boolean isFileEncryptionOrDecryption() {
        return nonNull(pathToFile) && isNull(data);
    }
}
