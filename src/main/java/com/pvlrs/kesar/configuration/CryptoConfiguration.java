package com.pvlrs.kesar.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.pvlrs.kesar.crypto")
public class CryptoConfiguration {

    /* Default salt, should be overridden by specifying the value
       of com.pvlrs.kesar.crypto.salt in application.properties */
    private String salt = "<da?hBv@uk4=d[Yf+y<9Bzq_N7!V-'m?";
}
