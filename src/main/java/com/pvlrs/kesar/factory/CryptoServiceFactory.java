package com.pvlrs.kesar.factory;

import com.pvlrs.kesar.crypto.PasswordBasedCryptoService;
import com.pvlrs.kesar.enums.CryptoServiceQualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CryptoServiceFactory {

    // Used to look up & select the appropriate implementation
    private Map<CryptoServiceQualifier, PasswordBasedCryptoService> cryptoServiceIndex;

    public CryptoServiceFactory(Set<PasswordBasedCryptoService> cryptoServices) {
        this.cryptoServiceIndex = cryptoServices.stream()
                .collect(Collectors.toMap(PasswordBasedCryptoService::getQualifier, Function.identity()));
    }

    public PasswordBasedCryptoService getByQualifier(CryptoServiceQualifier qualifier) {
        return this.cryptoServiceIndex.get(qualifier);
    }
}
