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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    // Configuración de CORS: todavía no hay frontend corriendo, pero la
    // dejamos lista para cuando React empiece a pegarle a la API desde
    // otro puerto (localhost:3000 con Create React App, localhost:5173
    // con Vite). Sin esto, el navegador bloquea los requests cross-origin
    // aunque Insomnia/Postman los dejen pasar sin problema.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuracion);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Solo el login es publico, sin token. El resto de /api/auth/**
                        // (por ejemplo cambiar-password) requiere estar logueado y
                        // cae en la regla anyRequest().authenticated() de mas abajo.
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // Calcular evaluacion y ABM de certificados: los dos roles.
                        .requestMatchers(HttpMethod.POST, "/api/evaluaciones/calcular/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.POST, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")
                        .requestMatchers(HttpMethod.DELETE, "/api/certificados/**").hasAnyRole("ADMIN", "ANALISTA")

                        // Catalogo maestro (Sistema/Algoritmo/Componente), parametros
                        // de riesgo, y CRUD manual de Evaluacion: solo ADMIN.
                        .requestMatchers(HttpMethod.POST, "/api/sistemas/**", "/api/algoritmos/**", "/api/componentes/**", "/api/evaluaciones", "/api/usuarios").hasRole("ADMIN")
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
