package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                           JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                           JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Login: publico, sin token.
                        .requestMatchers("/api/auth/**").permitAll()

                        // Calcular evaluacion y ABM de certificados: los dos roles.
                        .requestMatchers(HttpMethod.POST, "/api/evaluaciones/calcular/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.POST, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.DELETE, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")

                        // Catalogo maestro (Sistema/Algoritmo/Componente), parametros
                        // de riesgo, y CRUD manual de Evaluacion: solo ADMIN.
                        .requestMatchers(HttpMethod.POST, "/api/sistemas/**", "/api/algoritmos/**", "/api/componentes/**", "/api/evaluaciones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/sistemas/**", "/api/algoritmos/**", "/api/componentes/**", "/api/evaluaciones/**", "/api/parametros-riesgo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/sistemas/**", "/api/algoritmos/**", "/api/componentes/**", "/api/evaluaciones/**").hasRole("ADMIN")

                        // Lectura: los dos roles, en cualquier recurso.
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "ANALISTA")

                        // Cualquier otra cosa no contemplada arriba: hay que estar logueado igual.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
