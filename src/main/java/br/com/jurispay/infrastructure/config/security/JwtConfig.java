package br.com.jurispay.infrastructure.config.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configuração de JWT (JSON Web Token).
 * Cria beans para codificação e decodificação de tokens JWT usando secret simétrica.
 */
@Configuration
public class JwtConfig {

    @Value("${jurispay.security.jwt.secret}")
    private String jwtSecret;

    @Value("${jurispay.security.jwt.issuer}")
    private String jwtIssuer;

    @Value("${jurispay.security.jwt.expirationMinutes}")
    private Long expirationMinutes;

    /**
     * Cria SecretKey a partir da string de secret configurada.
     * Usa algoritmo HmacSHA256.
     *
     * @return SecretKey para assinatura e verificação de tokens JWT
     */
    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * Cria JwtEncoder usando Nimbus com secret simétrica.
     *
     * @param secretKey SecretKey para assinatura
     * @return JwtEncoder configurado
     */
    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    /**
     * Cria JwtDecoder usando Nimbus com secret simétrica.
     * Configurado para algoritmo HS256.
     *
     * @param secretKey SecretKey para verificação
     * @return JwtDecoder configurado
     */
    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    /**
     * Getter para issuer (pode ser usado em outros beans).
     *
     * @return issuer do JWT
     */
    public String getJwtIssuer() {
        return jwtIssuer;
    }

    /**
     * Getter para expirationMinutes (pode ser usado em outros beans).
     *
     * @return minutos de expiração do token
     */
    public Long getExpirationMinutes() {
        return expirationMinutes;
    }
}

