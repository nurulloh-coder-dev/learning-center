package org.example.crm.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.crm.entity.login.TokenDto;
import org.example.crm.entity.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.access.token.expire.date:180}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.token.expire.date:86400}")
    private Long refreshTokenExpiration;

    @Value("${jwt.access.token.secretKey}")
    private String secretKey;

    @Value("${jwt.refresh.token.secretKey}")
    private String refreshToken;

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims extractClaimsIgnoreExpiry(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .clockSkewSeconds(Long.MAX_VALUE / 1000)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(Claims claims) {
        String subject = claims.getSubject();
        return subject != null && !claims.getExpiration().before(new Date());
    }

    public Map<String, Object> prepareClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());
        if (user.getOrganizationId() != null) {
            claims.put("organizationId", user.getOrganizationId());
        }
        return claims;
    }

    //    public Map<String, Object> prepareClaims(UserDto user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", user.id());
//        claims.put("role", user.role().name());
//        return claims;
//    }

    /**
     * Original entry point — unchanged behavior for every existing caller.
     * "access" uses accessTokenExpiration, anything else uses refreshTokenExpiration.
     */
    public TokenDto generateToken(String key, Map<String, Object> stringObjectMap, String duration) {
        long expirySeconds = duration.equals("access") ? accessTokenExpiration : refreshTokenExpiration;
        return generateToken(key, stringObjectMap, expirySeconds);
    }

    /**
     * New overload — lets callers specify an exact expiry in seconds.
     * Used for remember-me, where the refresh token's lifetime needs to vary
     * (30 days if remembered, a short session otherwise) instead of using
     * the fixed refreshTokenExpiration default.
     */
    public TokenDto generateToken(String key, Map<String, Object> stringObjectMap, long expirySeconds) {
        long expiryTimeStamp = System.currentTimeMillis() + (expirySeconds * 1000);
        Date expiry = new Date(expiryTimeStamp);
        String token = Jwts.builder()
                .claims(stringObjectMap)
                .subject(key)
                .expiration(expiry)
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .compact();

        return TokenDto.builder()
                .token(token)
                .expiry(expiryTimeStamp)
                .build();
    }

}
