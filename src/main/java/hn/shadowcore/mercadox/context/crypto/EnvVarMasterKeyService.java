package hn.shadowcore.mercadox.context.crypto;

import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyOperationException;
import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

/**
 * Phase-1 master key: a static AES-256 key held in config (env var), used to wrap/unwrap
 * per-value data keys via AES Key Wrap (RFC 3394) — deterministic, integrity-checked, and
 * purpose-built for wrapping a symmetric key under another symmetric key, so the shape of
 * wrap/unwrap here matches what a real KMS's key-wrap operation does. Swappable later for a
 * KMS-backed {@link MasterKeyService} (see {@link EncryptionAutoConfiguration}).
 */
public class EnvVarMasterKeyService implements MasterKeyService {

    private static final String WRAP_ALGORITHM = "AESWrap";
    private static final int AES_256_KEY_BYTES = 32;

    private final SecretKey masterKey;

    public EnvVarMasterKeyService(MasterKeyProperties properties) {
        if (properties == null || properties.value() == null || properties.value().isBlank()) {
            throw new IllegalArgumentException(
                    "encryption.master-key.value must contain a Base64-encoded AES-256 key"
            );
        }

        final byte[] raw;
        try {
            raw = Base64.getDecoder().decode(properties.value());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "encryption.master-key.value must be valid Base64",
                    exception
            );
        }

        if (raw.length != AES_256_KEY_BYTES) {
            throw new IllegalArgumentException(
                    "encryption.master-key.value must decode to exactly 32 bytes for AES-256"
            );
        }

        this.masterKey = new SecretKeySpec(raw, "AES");
    }

    @Override
    public byte[] wrap(byte[] dataKey) {
        try {
            Cipher cipher = Cipher.getInstance(WRAP_ALGORITHM);
            cipher.init(Cipher.WRAP_MODE, masterKey);
            return cipher.wrap(new SecretKeySpec(dataKey, "AES"));
        } catch (GeneralSecurityException e) {
            throw new MasterKeyOperationException("Failed to wrap data key", e);
        }
    }

    @Override
    public byte[] unwrap(byte[] wrappedDataKey) {
        try {
            Cipher cipher = Cipher.getInstance(WRAP_ALGORITHM);
            cipher.init(Cipher.UNWRAP_MODE, masterKey);
            Key dek = cipher.unwrap(wrappedDataKey, "AES", Cipher.SECRET_KEY);
            return dek.getEncoded();
        } catch (GeneralSecurityException e) {
            throw new MasterKeyOperationException("Failed to unwrap data key", e);
        }
    }
}
