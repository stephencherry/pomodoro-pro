package com.platform.pomodoropro.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.pomodoropro.entity.model.ROLE;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${app.authentication.jwt.secret}")
    private String secret;

    @Value("${app.authentication.jwt.issuer}")
    private String issuer;

    @Value("${app.authentication.jwt.token.validity.seconds}")
    private long tokenValidityInMilliseconds;

    private final ObjectMapper objectMapper;

    /**
     * Creating JWT TOKEN from the authentication object
     * @param authentication
     * @return
     */

    public String createToken(Authentication authentication) {
       // AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds * 1000);
        return Jwts.builder()
                .setSubject(((AppUserDetails)authentication.getPrincipal()).getUser().getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuer(issuer)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    /**
     * Checks that the token is still valid and has not been manipulated
     */
    boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token trace: {}", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token trace: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token trace: {}", e.getMessage(), e);
        } catch (IllegalArgumentException | io.jsonwebtoken.security.SecurityException e) {
            logger.error("JWT token compact of handler are invalid trace: {}", e.getMessage(), e);
        }
        return false;
    }

    Authentication getAuthentication(String token, String uri) {
        Claims claims = getClaims(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
        boolean notAuthorized = false;
        if (!claims.get(AUTHORITIES_KEY).equals("")) {
            authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        User principal = new User(claims.getSubject(), "", authorities);

        if(uri.contains("admin")){
            log.info(principal.getAuthorities()+"");

            boolean hasRole = principal.getAuthorities()
                    .stream()
                    .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase(String.valueOf(ROLE.ADMIN)));

            if (!hasRole){
                notAuthorized = true;
            }
        }

        if (notAuthorized){
            return null;
        }else{
            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        }
    }

    /**
     * Decodes the token
     */
    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .parseClaimsJws(token);
    }
}
