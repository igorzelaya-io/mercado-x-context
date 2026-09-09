package hn.shadowcore.mercadox.context.crypto;

import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyOperationException;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvVarMasterKeyServiceTest {

    @Test
    void constructor_withoutAConfiguredKey_failsWithConfigurationMessage() {
        assertThatThrownBy(() -> new EnvVarMasterKeyService(new MasterKeyProperties(null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("encryption.master-key.value");
    }

    @Test
    void constructor_withMalformedBase64_failsWithConfigurationMessage() {
        assertThatThrownBy(() -> new EnvVarMasterKeyService(new MasterKeyProperties("not-base64!")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("valid Base64");
    }

    @Test
    void constructor_withNonAes256Key_failsWithConfigurationMessage() {
        String aes128Key = Base64.getEncoder().encodeToString(new byte[16]);

        assertThatThrownBy(() -> new EnvVarMasterKeyService(new MasterKeyProperties(aes128Key)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 32 bytes");
    }

    @Test
    void wrapThenUnwrap_returnsOriginalDataKeyBytes() throws Exception {
        EnvVarMasterKeyService service = new EnvVarMasterKeyService(randomKeyProperties());
        byte[] dek = randomAesKeyBytes();

        byte[] wrapped = service.wrap(dek);
        byte[] unwrapped = service.unwrap(wrapped);

        assertThat(unwrapped).isEqualTo(dek);
        assertThat(wrapped).isNotEqualTo(dek);
    }

    @Test
    void unwrap_underADifferentMasterKey_fails() throws Exception {
        EnvVarMasterKeyService serviceA = new EnvVarMasterKeyService(randomKeyProperties());
        EnvVarMasterKeyService serviceB = new EnvVarMasterKeyService(randomKeyProperties());
        byte[] dek = randomAesKeyBytes();

        byte[] wrappedUnderA = serviceA.wrap(dek);

        assertThatThrownBy(() -> serviceB.unwrap(wrappedUnderA))
                .isInstanceOf(MasterKeyOperationException.class);
    }

    private static MasterKeyProperties randomKeyProperties() throws Exception {
        return new MasterKeyProperties(Base64.getEncoder().encodeToString(randomAesKeyBytes()));
    }

    private static byte[] randomAesKeyBytes() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom());
        return keyGenerator.generateKey().getEncoded();
    }
}
