package com.fittrack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed JWT configuration bound from application.yml.
 * Never hardcode JWT secrets — always use environment variables in prod.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fittrack.jwt")
public class JwtProperties {
    private String secret;
    private long expirationMs;
    private long refreshExpirationMs;
}
