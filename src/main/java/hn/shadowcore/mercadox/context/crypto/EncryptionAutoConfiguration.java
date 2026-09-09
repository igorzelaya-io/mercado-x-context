package hn.shadowcore.mercadox.context.crypto;

import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(MasterKeyProperties.class)
@ConditionalOnProperty(
        prefix = "encryption.master-key",
        name = "value"
)
public class EncryptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MasterKeyService.class)
    public MasterKeyService masterKeyService(MasterKeyProperties properties) {
        return new EnvVarMasterKeyService(properties);
    }
}
