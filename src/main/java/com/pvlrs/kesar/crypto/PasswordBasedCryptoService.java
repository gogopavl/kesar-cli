package com.pvlrs.kesar.crypto;

import com.pvlrs.kesar.enums.CryptoServiceQualifier;

public interface PasswordBasedCryptoService {

    CryptoServiceQualifier getQualifier();

    String encrypt(String input, String password);

    String decrypt(String input, String password);
}
