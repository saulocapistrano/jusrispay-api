package br.com.jurispay.infrastructure.security.auth;

import br.com.jurispay.infrastructure.config.security.JwtConfig;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para geração de tokens JWT.
 * Responsável por criar tokens com claims apropriados.
 */
@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Gera um token JWT para o usuário e roles especificados.
     *
     * @param username nome de usuário (será usado como 'sub')
     * @param roles    coleção de roles do usuário (pode conter "ROLE_" como prefixo)
     * @return token JWT como string
     */
    public String generateToken(String username, Collection<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(JwtConfig.EXPIRES_IN_SECONDS);

        // Remove prefixo "ROLE_" se existir e converte para lista
        List<String> cleanRoles = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(JwtConfig.ISSUER)
                .subject(username)
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("roles", cleanRoles) // Roles sem prefixo "ROLE_"
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}

