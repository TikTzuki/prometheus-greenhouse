package tik.prometheus.rest.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tik.prometheus.rest.repositories.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    @Autowired
    UserRepository userRepository;

    @Value("${spring.application.name}")
    String secret;

    public String getBase64UrlSecret() {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getBase64UrlSecret())
                .parseClaimsJws(token).getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String audience) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", userRepository.findByUserName(username).getScope());
        return createToken(claims, username, audience);
    }

    private String createToken(Map<String, Object> claims, String subject, String audience) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("rest-service")
                .setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, getBase64UrlSecret()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFroJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getBase64UrlSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
