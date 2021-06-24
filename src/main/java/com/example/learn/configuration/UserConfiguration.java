package com.example.learn.configuration;

import com.example.learn.filter.JwtAuthenticationAccessDenied;
import com.example.learn.filter.JwtAuthentificationEntryPoint;
import com.example.learn.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.learn.constant.SecurityConstant.PUBLIC_URL;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserConfiguration extends WebSecurityConfigurerAdapter {
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    private JwtAuthentificationEntryPoint jwtAuthentificationEntryPoint;
    private JwtAuthenticationAccessDenied jwtAuthenticationAccessDenied;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserConfiguration(JwtAuthorizationFilter jwtAuthorizationFilter,
                             JwtAuthentificationEntryPoint jwtAuthentificationEntryPoint,
                             JwtAuthenticationAccessDenied jwtAuthenticationAccessDenied,
                             @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                             BCryptPasswordEncoder encoder) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAuthentificationEntryPoint = jwtAuthentificationEntryPoint;
        this.jwtAuthenticationAccessDenied = jwtAuthenticationAccessDenied;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
        .sessionManagement().sessionCreationPolicy(STATELESS)
        .and().authorizeRequests().antMatchers(PUBLIC_URL).permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(jwtAuthenticationAccessDenied)
        .authenticationEntryPoint(jwtAuthentificationEntryPoint)
        .and()
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
