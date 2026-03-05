package com.vendora.vendorapos.util;

import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final int TOKEN_VALIDITY = 3600 * 5;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserService userService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Validate the token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        User user = userService.findByEmail(userDetails.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .claim("tenantId",user.getBusiness().getId())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY*1000))
                .signWith(getSigningKey())
                .compact();

    }

    public Long extractTenantId(String token) {
        Object tenantId = getAllClaimsFromToken(token).get("tenantId");
        if (tenantId == null) return null;
        return Long.parseLong(tenantId.toString());
    }
}
