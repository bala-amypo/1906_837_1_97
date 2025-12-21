package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF (needed for Swagger & REST APIs)
            .csrf(csrf -> csrf.disable())

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ Swagger UI & OpenAPI
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                // ✅ Auth APIs (login & register)
                .requestMatchers("/auth/**").permitAll()

                // ✅ Public verification APIs
                .requestMatchers("/verify/**").permitAll()

                // 🔐 Template APIs (FIXED)
                .requestMatchers("/templates/**").authenticated()

                // 🔐 Student APIs
                .requestMatchers("/students/**").authenticated()

                // 🔐 Certificate APIs
                .requestMatchers("/certificates/**").authenticated()

                // 🔐 Everything else
                .anyRequest().authenticated()
            )

            // ✅ Stateless JWT session
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ Authentication provider
            .authenticationProvider(authenticationProvider)

            // ✅ JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
