package com.example.learn.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.learn.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.learn.domain.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvided {

    @Value("$jwt.secret")
    private String secret;

    public String GenerateToken(UserPrincipal userPrincipal){
        String[] claims=getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(GET_ARRAYS_LLC)
                .withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withArrayClaim(AUTHORITIES,claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .withSubject(userPrincipal.getUsername())
                .sign(HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token){
        String[] claims=getAuthoritesFromtoken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    public String getSubject(String token){
        JWTVerifier verifier=Verify(token);
        return verifier.verify(token).getSubject();
    }

    public boolean isAuthentic(String username, String token){
        JWTVerifier verifier =Verify(token);
        return StringUtils.isNotEmpty(username) && !NotExpired(token,verifier);
    }

    public Authentication Authentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,null,authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private boolean NotExpired(String token, JWTVerifier verifier) {
        return verifier.verify(token).getExpiresAt().before(new Date());
    }

    private String[] getAuthoritesFromtoken(String token) {
        JWTVerifier verifier=Verify(token);
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier Verify(String token) {
        JWTVerifier verifier;
        try {
            Algorithm algorithm=HMAC512(secret);
            verifier=JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return  verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String>claims=new ArrayList<>();
        for (GrantedAuthority authority:userPrincipal.getAuthorities()){
            claims.add(authority.getAuthority());
        }
        return claims.toArray(new String[0]);
    }

}
