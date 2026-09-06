package hn.shadowcore.mercadox.context.crypto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "encryption.master-key")
public record MasterKeyProperties(
        String value
) { }
