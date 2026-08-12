package hn.shadowcore.mercadox.context.security;

import java.util.List;

public record VerifiedJwt(
        String email,
        String orgId,
        List<String> roles
) {
}