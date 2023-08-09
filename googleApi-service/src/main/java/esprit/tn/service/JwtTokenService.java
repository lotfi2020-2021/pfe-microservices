package esprit.tn.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import  esprit.tn.common.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    @Value("jwt.secret")
    private String jwtSecret;



    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getSubjectFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getSubject();
    }

    public Boolean isTokenValid(String email, String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return StringUtils.isNotEmpty(email) && !isTokenExpired(jwtVerifier, token);
    }

    private Boolean isTokenExpired(JWTVerifier jwtVerifier, String token) {
        Date expiration = jwtVerifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }



    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getClaim(AppConstants.AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJwtVerifier() {
        JWTVerifier jwtVerifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            jwtVerifier = JWT.require(algorithm).build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(AppConstants.TOKEN_UNVERIFIABLE);
        }
        return jwtVerifier;
    }
}
