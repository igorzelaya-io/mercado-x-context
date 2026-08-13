package hn.shadowcore.mercadox.context.config;


import hn.shadowcore.mercadox.context.security.JwtVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPublicKey;


@Configuration
public class JwtConfig {

    @Bean
    @ConditionalOnMissingBean(JwtVerifier.class)
    public JwtVerifier jwtVerifier(RSAPublicKey publicKey) {
        return new JwtVerifier(publicKey);
    }

}
