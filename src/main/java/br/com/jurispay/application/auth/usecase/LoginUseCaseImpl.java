package br.com.jurispay.application.auth.usecase;

import br.com.jurispay.api.dto.auth.TokenResponse;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.infrastructure.config.security.JwtConfig;
import br.com.jurispay.infrastructure.security.auth.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso de login.
 * Autentica credenciais e gera token JWT.
 */
@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public LoginUseCaseImpl(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public TokenResponse login(String username, String password) {
        try {
            // Autentica credenciais
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Extrai roles das authorities (mantém "ROLE_" pois será removido no JwtTokenService)
            Collection<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Gera token JWT
            String token = jwtTokenService.generateToken(username, roles);

            // Calcula expiresIn em segundos (1 hora)
            long expiresIn = JwtConfig.EXPIRES_IN_SECONDS;

            return TokenResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .build();

        } catch (BadCredentialsException e) {
            // Não logar detalhes sensíveis (LGPD)
            // Re-lança como BadCredentialsException para ser tratada pelo Spring Security
            // O SecurityConfig ou handler deve retornar 401
            throw e;
        }
    }
}

