package hn.shadowcore.mercadoxcontext.utils;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Role;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    private final SecretKey secretKey;

    private final JwtParser jwtParser;

    private final long jwtExpirationMs;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(String secret, long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        final List<String> userRoles = user.getUserRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("orgId", user.getOrganization().getId().toString())
                .claim("roles", userRoles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public String getOrgIdFromToken(String token) {
        return getAllClaims(token).get("orgId", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return getAllClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        }
        catch(JwtException ignored) {
            logger.error("JWT was invalid.");
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload();
    }

}
