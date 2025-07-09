package hn.shadowcore.mercadoxcontext.config;


import hn.shadowcore.mercadoxcontext.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration}")
    private long jwtExpirationMs;
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecret, jwtExpirationMs);
    }

}
