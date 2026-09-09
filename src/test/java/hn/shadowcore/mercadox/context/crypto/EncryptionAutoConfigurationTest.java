package hn.shadowcore.mercadox.context.crypto;

import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptionAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(EncryptionAutoConfiguration.class));

    @Test
    void missingMasterKey_doesNotCreateMasterKeyService() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).doesNotHaveBean(MasterKeyService.class);
        });
    }

    @Test
    void configuredMasterKey_createsMasterKeyService() {
        String masterKey = Base64.getEncoder().encodeToString(new byte[32]);

        contextRunner
                .withPropertyValues("encryption.master-key.value=" + masterKey)
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).hasSingleBean(MasterKeyService.class);
                    assertThat(context).getBean(MasterKeyService.class)
                            .isInstanceOf(EnvVarMasterKeyService.class);
                });
    }

    @Test
    void blankMasterKey_failsWithConfigurationMessage() {
        contextRunner
                .withPropertyValues("encryption.master-key.value=")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure())
                            .rootCause()
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("encryption.master-key.value");
                });
    }
}
