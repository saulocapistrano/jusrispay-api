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
import java.util.Base64;

/**
 * Configuração de JWT (JSON Web Token).
 * Cria beans para codificação e decodificação de tokens JWT usando secret simétrica.
 */
@Configuration
public class JwtConfig {

    @Value("${jurispay.security.jwt.secret}")
    private String jwtSecret;

    /**
     * Issuer do JWT.
     */
    public static final String ISSUER = "jurispay-api";

    /**
     * Tempo de expiração em segundos (1 hora).
     */
    public static final long EXPIRES_IN_SECONDS = 3600L;

    /**
     * Cria SecretKey a partir da string de secret.
     * Aceita tanto Base64 quanto string simples.
     * Se for Base64 válido, decodifica; caso contrário, usa a string diretamente como UTF-8.
     * Usa algoritmo HmacSHA256 (requer pelo menos 32 bytes = 256 bits).
     *
     * @return SecretKey para assinatura e verificação de tokens JWT
     */
    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes;
        
        // Tenta decodificar como Base64 primeiro
        try {
            byte[] decoded = Base64.getDecoder().decode(jwtSecret);
            // Se decodificou com sucesso e tem pelo menos 32 bytes, usa
            if (decoded.length >= 32) {
                return new SecretKeySpec(decoded, "HmacSHA256");
            }
        } catch (IllegalArgumentException e) {
            // Não é Base64 válido, continua para usar como string simples
        }
        
        // Usa a string diretamente como bytes (UTF-8)
        // Garante que tenha pelo menos 32 caracteres para segurança mínima
        if (jwtSecret.length() < 32) {
            throw new IllegalArgumentException(
                "JWT secret deve ter pelo menos 32 caracteres (ou ser Base64 de pelo menos 32 bytes). " +
                "Atual: " + jwtSecret.length() + " caracteres");
        }
        
        // Converte string para bytes UTF-8 e garante exatamente 32 bytes
        keyBytes = jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        
        // HMAC-SHA256 requer exatamente 32 bytes (256 bits)
        // Se tiver mais, trunca; se tiver menos, preenche com zeros (não deve acontecer se length >= 32)
        byte[] finalKey = new byte[32];
        int copyLength = Math.min(keyBytes.length, 32);
        System.arraycopy(keyBytes, 0, finalKey, 0, copyLength);
        
        // Se a string tinha menos de 32 bytes (raro, mas possível com caracteres especiais),
        // preenche o resto com hash da string original para garantir segurança
        if (copyLength < 32) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                System.arraycopy(hash, 0, finalKey, copyLength, 32 - copyLength);
            } catch (java.security.NoSuchAlgorithmException e) {
                // Fallback: repete a string
                for (int i = copyLength; i < 32; i++) {
                    finalKey[i] = keyBytes[i % keyBytes.length];
                }
            }
        }
        
        return new SecretKeySpec(finalKey, "HmacSHA256");
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
     * O algoritmo HS256 é inferido automaticamente do SecretKey.
     *
     * @param secretKey SecretKey para verificação
     * @return JwtDecoder configurado
     */
    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}

