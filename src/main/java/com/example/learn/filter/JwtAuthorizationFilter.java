package com.example.learn.filter;

import com.example.learn.constant.SecurityConstant;
import com.example.learn.util.JWTTokenProvided;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.example.learn.constant.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvided jwtTokenProvided;

    public JwtAuthorizationFilter(JWTTokenProvided jwtTokenProvided) {
        this.jwtTokenProvided = jwtTokenProvided;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
                response.setStatus(HttpStatus.OK.value());
            }
            else{
                String authorizationHeader=request.getHeader(AUTHORIZATION);
                if(authorizationHeader==null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                    filterChain.doFilter(request,response);
                    return;
                }
                String token=AUTHORIZATION.substring(TOKEN_PREFIX.length());
                String username = jwtTokenProvided.getSubject(token);
                if(jwtTokenProvided.isAuthentic(username,token)){
                    List<GrantedAuthority> authority=jwtTokenProvided.getAuthoritiesFromToken(token);
                    Authentication authentication=jwtTokenProvided.Authentication(username,authority,request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request,response);
    }
}
