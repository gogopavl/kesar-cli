package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.command.arguments.InputArguments;
import com.pvlrs.kesar.exception.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final PasswordBasedCryptoService cryptoService;

    public EncryptionService(PasswordBasedCryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public String encryptData(InputArguments inputArguments) {
        if (inputArguments.isFileEncryption()) {
            throw new NotImplementedException("This feature hasn't been implemented yet...probably next commit!");
        } else {
            return cryptoService.encrypt(inputArguments.getData(), inputArguments.getPassword());
        }
    }
}
