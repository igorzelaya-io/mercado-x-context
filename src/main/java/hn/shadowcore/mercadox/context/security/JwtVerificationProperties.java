package hn.shadowcore.mercadox.context.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtVerificationProperties(
        Resource publicKeyLocation
) { }