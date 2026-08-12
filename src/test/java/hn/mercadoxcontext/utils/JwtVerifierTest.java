package hn.mercadoxcontext.utils;

import hn.shadowcore.mercadox.context.security.JwtVerifier;
import hn.shadowcore.mercadox.context.security.VerifiedJwt;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtVerifierTest {

    private static final String ISSUER = "mercadox-oauth";

    private JwtVerifier jwtVerifier;
    private RSAPrivateKey privateKey;

    @BeforeEach
    void init() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        jwtVerifier = new JwtVerifier((RSAPublicKey) keyPair.getPublic());
    }

    @Test
    void shouldVerifyValidTokenAndExtractClaims() {
        String email = "user@mercadox.com";
        String orgId = UUID.randomUUID().toString();
        List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER");

        String token = buildToken(email, orgId, roles);

        VerifiedJwt result = jwtVerifier.verify(token);

        assertThat(result.email()).isEqualTo(email);
        assertThat(result.orgId()).isEqualTo(orgId);
        assertThat(result.roles()).containsExactlyInAnyOrderElementsOf(roles);
    }

    @Test
    void shouldReturnTrueForValidToken() {
        String token = buildToken("user@mercadox.com", UUID.randomUUID().toString(), List.of());
        assertThat(jwtVerifier.validateToken(token)).isTrue();
    }

    @Test
    void shouldReturnFalseForMalformedToken() {
        assertThat(jwtVerifier.validateToken("not.a.jwt")).isFalse();
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        Date past = new Date(System.currentTimeMillis() - 10_000);
        String token = Jwts.builder()
                .subject("user@mercadox.com")
                .issuer(ISSUER)
                .claim("orgId", UUID.randomUUID().toString())
                .claim("roles", List.of())
                .issuedAt(past)
                .expiration(past)
                .signWith(privateKey)
                .compact();

        assertThat(jwtVerifier.validateToken(token)).isFalse();
    }

    @Test
    void shouldReturnFalseForWrongIssuer() {
        String token = Jwts.builder()
                .subject("user@mercadox.com")
                .issuer("rogue-issuer")
                .claim("orgId", UUID.randomUUID().toString())
                .claim("roles", List.of())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(privateKey)
                .compact();

        assertThat(jwtVerifier.validateToken(token)).isFalse();
    }

    private String buildToken(String email, String orgId, List<String> roles) {
        return Jwts.builder()
                .subject(email)
                .issuer(ISSUER)
                .claim("orgId", orgId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(privateKey)
                .compact();
    }
}
