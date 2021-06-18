package com.example.learn.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.learn.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.learn.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTTokenProvided {
    @Value("${jwt.secret}")
    private String secret;

    public String generateJWTToken(UserPrincipal userPrincipal){
        String[] permissions=getPermisionsFromUserPrincipal(userPrincipal);
        return JWT.create().withIssuer(GET_ARRAYS_LLC)    //the companyName
                .withAudience(GET_ARRAYS_ADMINISTRATION)   //for which audience
                .withIssuedAt(new Date())                  //the day he connected
                .withSubject(userPrincipal.getUsername()) //it got to be unique
                .withArrayClaim(AUTHORITIES,permissions)         //claims are the authorites that the user have
                .withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME)) //the day or the moment u want the token to be expired
                .sign(HMAC512(secret.getBytes()));
    }
    public List<GrantedAuthority> getAuthorites(String token){
        String[] permisssions=getPermisionsFromToken(token);
        return stream(permisssions).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String[] getPermisionsFromToken(String token) {
        JWTVerifier verifier=getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
            JWTVerifier verifier;
            try {
                Algorithm algorithm=HMAC512(secret);
                verifier =JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
            }catch (JWTVerificationException exception){
                    throw  new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
            }
            return verifier;
    }

    private String[] getPermisionsFromUserPrincipal(UserPrincipal userPrincipal) {
        List<String> authorities= new ArrayList<>();
        for (GrantedAuthority grantedAuthority:userPrincipal.getAuthorities()) {
                authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

}
