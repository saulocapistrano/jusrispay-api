package br.com.jurispay.infrastructure.security.auth;

import br.com.jurispay.infrastructure.config.security.JwtConfig;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Serviço para geração de tokens JWT.
 * Responsável por criar tokens com claims apropriados.
 */
@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final Long expirationMinutes;

    public JwtTokenService(JwtEncoder jwtEncoder, JwtConfig jwtConfig) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = jwtConfig.getJwtIssuer();
        this.expirationMinutes = jwtConfig.getExpirationMinutes();
    }

    /**
     * Gera um token JWT para o usuário e roles especificados.
     *
     * @param username nome de usuário (será usado como 'sub')
     * @param roles    coleção de roles do usuário
     * @return token JWT como string
     */
    public String generateToken(String username, Collection<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60);

        // Converte roles para scope (formato OAuth2: espaço separado)
        String scope = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(username)
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("scope", scope)
                .claim("roles", roles) // Mantém roles como array também
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

