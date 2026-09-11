package hn.alturaforge.mercadox.context.filter;

import hn.alturaforge.mercadox.context.security.JwtVerifier;
import hn.alturaforge.mercadox.context.security.VerifiedJwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Profile("!test")
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String VERIFIED_JWT_ATTRIBUTE = "hn.alturaforge.mercadox.context.VERIFIED_JWT";

    private final JwtVerifier jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);

            if (jwtUtil.validateToken(jwt)) {

                VerifiedJwt verified = jwtUtil.verify(jwt);
                request.setAttribute(VERIFIED_JWT_ATTRIBUTE, verified);
                String email = verified.email();
                List<String> roles = verified.roles();

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}