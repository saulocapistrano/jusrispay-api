package br.com.jurispay.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação usando Spring Security 6+.
 * Utiliza Resource Server JWT para autenticação stateless.
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
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Permite requisições OPTIONS (preflight CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Endpoints públicos: autenticação e documentação
                .requestMatchers(
                    "/api/auth/login",
                    "/",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/actuator/**"
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

                // Configuração do sistema (branding/contatos)
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/api/system-config/**"
                ).hasAnyRole("ADMIN", "OPERATOR", "AUDITOR")

                .requestMatchers(
                    org.springframework.http.HttpMethod.PUT,
                    "/api/system-config/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    org.springframework.http.HttpMethod.POST,
                    "/api/system-config/**"
                ).hasRole("ADMIN")
                
                // Endpoints de relatórios - leitura
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/api/reports/**"
                ).hasAnyRole("ADMIN", "OPERATOR", "AUDITOR")
                
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
                
                // Qualquer outra requisição requer autenticação JWT
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe AuthenticationManager como bean para uso em use cases.
     *
     * @param config configuração de autenticação
     * @return AuthenticationManager
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

