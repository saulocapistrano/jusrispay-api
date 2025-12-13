package br.com.jurispay.application.auth.usecase;

import br.com.jurispay.api.dto.auth.TokenResponse;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.infrastructure.config.security.JwtConfig;
import br.com.jurispay.infrastructure.security.auth.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para LoginUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void shouldReturnTokenResponseWhenCredentialsAreValid() {
        // Given
        String username = "admin";
        String password = "admin123";
        String token = "test-token";
        long expirationMinutes = 60L;

        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(jwtTokenService.generateToken(username, List.of("ROLE_ADMIN"))).thenReturn(token);
        when(jwtConfig.getExpirationMinutes()).thenReturn(expirationMinutes);

        // When
        TokenResponse response = loginUseCase.login(username, password);

        // Then
        assertNotNull(response);
        assertEquals(token, response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600L, response.getExpiresIn());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenService).generateToken(username, List.of("ROLE_ADMIN"));
    }

    @Test
    void shouldThrowValidationExceptionWhenCredentialsAreInvalid() {
        // Given
        String username = "invalid";
        String password = "wrong";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When / Then
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> loginUseCase.login(username, password)
        );

        assertEquals("Credenciais inválidas.", exception.getMessage());
        verify(jwtTokenService, never()).generateToken(anyString(), any());
    }
}

