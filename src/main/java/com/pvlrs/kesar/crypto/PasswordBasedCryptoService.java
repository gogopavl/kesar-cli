package com.pvlrs.kesar.crypto;

public interface PasswordBasedCryptoService {

    String encrypt(String plainTextInput, String password);

    String decrypt(String encryptedInput, String password);
}
