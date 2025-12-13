package br.com.jurispay.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação usando Spring Security 6+.
 * Utiliza o modelo novo de SecurityFilterChain.
 * 
 * <p>Roles:
 * <ul>
 *   <li>ROLE_ADMIN: acesso total</li>
 *   <li>ROLE_OPERATOR: operações de escrita e leitura</li>
 *   <li>ROLE_AUDITOR: apenas leitura</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (Swagger e Actuator)
                .requestMatchers(
                    "/",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                
                // Download de documentos (LGPD) - apenas ADMIN e OPERATOR
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/api/documents/*/download"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                // Endpoints de contrato - leitura
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/api/contracts/message/**"
                ).hasAnyRole("ADMIN", "OPERATOR", "AUDITOR")
                
                // Endpoints de contrato - escrita
                .requestMatchers(
                    org.springframework.http.HttpMethod.POST,
                    "/api/contracts/pdf/**"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                // Endpoints de escrita (POST/PATCH/PUT/DELETE) - ADMIN e OPERATOR
                .requestMatchers(
                    org.springframework.http.HttpMethod.POST,
                    "/api/customers/**",
                    "/api/loans/**",
                    "/api/payments/**",
                    "/api/documents/**",
                    "/api/credit-analyses/**",
                    "/api/collections/**"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                .requestMatchers(
                    org.springframework.http.HttpMethod.PATCH,
                    "/api/customers/**",
                    "/api/loans/**",
                    "/api/payments/**",
                    "/api/documents/**",
                    "/api/credit-analyses/**",
                    "/api/collections/**"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                .requestMatchers(
                    org.springframework.http.HttpMethod.PUT,
                    "/api/customers/**",
                    "/api/loans/**",
                    "/api/payments/**",
                    "/api/documents/**",
                    "/api/credit-analyses/**",
                    "/api/collections/**"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                .requestMatchers(
                    org.springframework.http.HttpMethod.DELETE,
                    "/api/customers/**",
                    "/api/loans/**",
                    "/api/payments/**",
                    "/api/documents/**",
                    "/api/credit-analyses/**",
                    "/api/collections/**"
                ).hasAnyRole("ADMIN", "OPERATOR")
                
                // Endpoints de leitura (GET) - ADMIN, OPERATOR e AUDITOR
                // Exclui download que já foi configurado acima
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/api/customers/**",
                    "/api/loans/**",
                    "/api/payments/**",
                    "/api/documents/**",
                    "/api/credit-analyses/**",
                    "/api/collections/**"
                ).hasAnyRole("ADMIN", "OPERATOR", "AUDITOR")
                
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

