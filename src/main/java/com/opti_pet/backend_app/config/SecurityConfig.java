package com.opti_pet.backend_app.config;

import com.opti_pet.backend_app.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatchers(matchers -> matchers.requestMatchers(AntPathRequestMatcher.antMatcher("/**")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/api/v1/users/register", "/api/v1/auth/generateToken").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    @Bean
    public SecurityFilterChain managementFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .securityMatcher(AntPathRequestMatcher.antMatcher("/management/**"))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                )
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/management/**")))
                .headers(AbstractHttpConfigurer::disable)
                .build();
    }
}
