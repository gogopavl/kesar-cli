package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.command.arguments.InputArguments;
import com.pvlrs.kesar.exception.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class DecryptionService {

    private final PasswordBasedCryptoService cryptoService;

    public DecryptionService(PasswordBasedCryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public String decryptData(InputArguments inputArguments) {
        if (inputArguments.isFileEncryption()) {
            throw new NotImplementedException("This feature hasn't been implemented yet...probably next commit!");
        } else {
            return cryptoService.decrypt(inputArguments.getData(), inputArguments.getPassword());
        }
    }
}
