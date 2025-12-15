package br.com.jurispay.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração CORS para permitir requisições do frontend.
 * Configurável via propriedades application.properties.
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:3001,http://localhost:3000}")
    private String allowedOrigins;

    /**
     * Configuração CORS para permitir requisições do frontend.
     * Origens permitidas são configuráveis via propriedade app.cors.allowed-origins.
     *
     * @return CorsConfigurationSource configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Split por vírgula e trim para permitir múltiplas origens
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toList();
        
        // Com allowCredentials true, usar setAllowedOriginPatterns para evitar problemas
        // com múltiplas origens (Spring Security requer patterns quando credentials=true)
        configuration.setAllowedOriginPatterns(origins);
        
        // Métodos HTTP permitidos (incluindo OPTIONS para preflight)
        configuration.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));
        
        // Headers permitidos nas requisições
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
        // Headers expostos na resposta
        configuration.setExposedHeaders(Arrays.asList(
            "Location"
        ));
        
        // allowCredentials: true para permitir envio de tokens via Authorization header
        configuration.setAllowCredentials(true);
        
        // Cache de preflight por 1 hora (3600 segundos)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

