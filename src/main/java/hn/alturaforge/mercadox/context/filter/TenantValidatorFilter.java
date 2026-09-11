package hn.alturaforge.mercadox.context.filter;

import hn.alturaforge.mercadox.context.security.VerifiedJwt;
import hn.alturaforge.mercadox.context.utils.OrgIdContextHolder;
import hn.alturaforge.mercadox.context.validator.AnonymousTenantValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Must be registered after JwtAuthFilter in the security chain: it reads the
// already-verified JWT from the request attribute JwtAuthFilter sets, rather
// than re-parsing/re-verifying the token itself.
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(AnonymousTenantValidator.class)
public class TenantValidatorFilter extends OncePerRequestFilter {

    private final AnonymousTenantValidator tenantValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String orgId = resolveTenant(request);

            if(StringUtils.hasText(orgId)) {
                log.info("OrgID Context Holder set for value: {}", orgId);
                tenantValidator.validate(orgId);
                OrgIdContextHolder.setTenantId(orgId);
            } else {
                log.warn("Failed to add OrgID Context Holder value.");
            }
            filterChain.doFilter(request, response);
        }

        finally {
            OrgIdContextHolder.clear();
        }
    }

    private String resolveTenant(HttpServletRequest request) {

        Object verifiedJwtAttribute = request.getAttribute(JwtAuthFilter.VERIFIED_JWT_ATTRIBUTE);

        if (verifiedJwtAttribute instanceof VerifiedJwt verified) {
            return verified.orgId();
        }

        String uri = request.getRequestURI();

        // Example: /api/v1/public/orgs/{orgId}/leads
        if (uri.startsWith("/api/v1/public/orgs/")) {
            String[] segments = uri.split("/");
            if (segments.length >= 6) {
                return segments[5]; // index based on path
            }
        }

        return null;
    }
}
