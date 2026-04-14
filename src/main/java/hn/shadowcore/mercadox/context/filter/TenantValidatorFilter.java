package hn.shadowcore.mercadox.context.filter;

import hn.shadowcore.mercadox.context.utils.JwtUtil;
import hn.shadowcore.mercadox.context.utils.OrgIdContextHolder;
import hn.shadowcore.mercadox.context.validator.AnonymousTenantValidator;
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

@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(AnonymousTenantValidator.class)
public class TenantValidatorFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final AnonymousTenantValidator tenantValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String orgId = resolveTenant(request);

            if(StringUtils.hasText(orgId)) {
                log.info(String.format("OrgID Context Holder set for value: %s", orgId));
                tenantValidator.validate(orgId);
                OrgIdContextHolder.setTenantId(orgId);
            }
            log.warn("Failed to add OrgID Context Holder value.");
            filterChain.doFilter(request, response);
        }

        finally {
            OrgIdContextHolder.clear();
        }
    }

    private String resolveTenant(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwtUtil.validateToken(jwt)) {
                return jwtUtil.getOrgIdFromToken(jwt);
            }
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
