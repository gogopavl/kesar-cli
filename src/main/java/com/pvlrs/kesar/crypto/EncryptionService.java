package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.command.arguments.InputArguments;
import com.pvlrs.kesar.enums.CryptoServiceQualifier;
import com.pvlrs.kesar.factory.CryptoServiceFactory;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final CryptoServiceFactory cryptoServiceFactory;

    public EncryptionService(CryptoServiceFactory cryptoServiceFactory) {
        this.cryptoServiceFactory = cryptoServiceFactory;
    }

    public String encryptData(InputArguments inputArguments) {
        PasswordBasedCryptoService cryptoService;
        if (inputArguments.isFileEncryptionOrDecryption()) {
            cryptoService = cryptoServiceFactory.getByQualifier(CryptoServiceQualifier.AES_FILE_CRYPTO_SERVICE);
            return cryptoService.encrypt(inputArguments.getPathToFile(), inputArguments.getPassword());
        } else {
            cryptoService = cryptoServiceFactory.getByQualifier(CryptoServiceQualifier.AES_RAW_DATA_CRYPTO_SERVICE);
            return cryptoService.encrypt(inputArguments.getData(), inputArguments.getPassword());
        }
    }
}
