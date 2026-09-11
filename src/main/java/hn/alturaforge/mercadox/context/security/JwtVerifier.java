package hn.alturaforge.mercadox.context.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

public class JwtVerifier {

    private static final String ISSUER = "mercadox-oauth";
    // Must match JwtSigner's AUDIENCE in mercado-x-oauth — pins tokens to this platform's
    // API surface so a token minted for some other RS256-verifying audience (defense in
    // depth only; issuer + signature already cover the primary threat) can't be replayed here.
    private static final String AUDIENCE = "mercadox-api";

    private final JwtParser jwtParser;

    public JwtVerifier(RSAPublicKey publicKey) {
        this.jwtParser = Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(ISSUER)
                .requireAudience(AUDIENCE)
                .build();
    }

    public VerifiedJwt verify(String token) {

        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();

        return new VerifiedJwt(
                claims.getSubject(),
                claims.get("orgId", String.class),
                getRoles(claims)
        );
    }

    public boolean validateToken(String token) {
        try {
            verify(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> getRoles(Claims claims) {

        List<?> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(String::valueOf)
                .toList();
    }
}