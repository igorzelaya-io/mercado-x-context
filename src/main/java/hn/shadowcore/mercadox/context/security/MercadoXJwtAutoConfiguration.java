package hn.shadowcore.mercadox.context.security;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.security.interfaces.RSAPublicKey;

@AutoConfiguration
@EnableConfigurationProperties(JwtVerificationProperties.class)
public class MercadoXJwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RSAPublicKey.class)
    public RSAPublicKey jwtPublicKey(
            JwtVerificationProperties properties
    ) {
        return PublicKeyUtils.readPublicKey(
                properties.publicKeyLocation()
        );
    }

    @Bean
    @ConditionalOnMissingBean(JwtVerifier.class)
    public JwtVerifier jwtVerifier(
            RSAPublicKey publicKey
    ) {
        return new JwtVerifier(publicKey);
    }

}