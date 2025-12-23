package com.example.demo.config;  
  
import com.example.demo.security.JwtFilter; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.authentication.AuthenticationManager;  import  org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfigur 
ation; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import 
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;  
  
@Configuration  
public class SecurityConfig {  
  
    private final JwtFilter jwtFilter;  
  
    public SecurityConfig(JwtFilter jwtFilter) {         this.jwtFilter = jwtFilter;  
    }  
  
    @Bean     public PasswordEncoder passwordEncoder() {         return new 
BCryptPasswordEncoder();  
    }  
    @Bean     public AuthenticationManager authenticationManager(             AuthenticationConfiguration config) throws Exception {         return 
config.getAuthenticationManager();  
    }  
  
    @Bean     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  
  
        http  
// REST API → disable CSRF  
        .csrf(csrf -> csrf.disable())  
        // JWT → stateless  
            .sessionManagement(session ->                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  
  
            //   IMPORTANT FIX → allow CORS preflight  
            .cors(cors -> {})  
  
            .authorizeHttpRequests(auth -> auth  
  
                // allow browser & error page  
                .requestMatchers("/", "/error").permitAll()  
  
                // auth endpoints  
                .requestMatchers("/auth/**").permitAll()  
  
                // swagger  
                .requestMatchers(  
                       "/swagger-ui/**",  
                        "/swagger-ui.html",  
                        "/swagger-ui/index.html",  
                        "/v3/api-docs/**"  
                ).permitAll()  
  
                // allow OPTIONS for Swagger POST  
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()  
  
                // everything else needs JWT  
                .anyRequest().authenticated()  
  	)  
  
            // JWT filter  
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  
  
        return http.build();  
    }  
}  
