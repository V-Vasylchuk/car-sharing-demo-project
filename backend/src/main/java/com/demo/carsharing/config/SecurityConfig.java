package com.demo.carsharing.config;

import com.demo.carsharing.model.User;
import com.demo.carsharing.security.jwt.JwtTokenFilter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String MANAGER = User.Role.MANAGER.name();
    private static final String CUSTOMER = User.Role.CUSTOMER.name();

    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                HttpMethod.POST, "/register", "/login").permitAll()
                        .requestMatchers("/healthcheck", "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/me").hasAnyRole(MANAGER, CUSTOMER)
                        .requestMatchers("/users/{id}").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/users/{id}/role").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.POST, "/cars").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.GET, "/cars").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cars/{id}")
                        .hasAnyRole(MANAGER, CUSTOMER)
                        .requestMatchers(HttpMethod.PUT, "/cars/{id}").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/cars/{id}").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.POST, "/rentals", "/rentals/{id}/return")
                        .hasAnyRole(MANAGER, CUSTOMER)
                        .requestMatchers(HttpMethod.GET, "/rentals/{id}")
                        .hasAnyRole(MANAGER, CUSTOMER)
                        .requestMatchers(HttpMethod.GET, "/rentals").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.GET, "/rentals/").hasRole(MANAGER)
                        .requestMatchers(HttpMethod.POST, "/rentals").hasRole(MANAGER)
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000/",
                "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST",
                "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control",
                "Content-Type", "Origin"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
