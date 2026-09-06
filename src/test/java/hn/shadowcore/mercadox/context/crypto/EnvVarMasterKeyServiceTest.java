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
