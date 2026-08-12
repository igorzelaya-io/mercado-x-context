package hn.shadowcore.mercadox.context.security;

import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class PublicKeyUtils {

    private PublicKeyUtils() {
    }

    public static RSAPublicKey readPublicKey(Resource resource) {
        try {
            String key = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String normalizedKey = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder()
                    .decode(normalizedKey);

            KeyFactory keyFactory =
                    KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(
                    new X509EncodedKeySpec(decoded)
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to load JWT public key",
                    e
            );
        }
    }
}