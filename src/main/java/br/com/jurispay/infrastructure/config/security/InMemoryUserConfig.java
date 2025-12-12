package br.com.jurispay.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de usuários em memória para ambiente de desenvolvimento.
 * 
 * <p><strong>ATENÇÃO:</strong> Esta configuração é apenas para ambiente de desenvolvimento.
 * Em produção, deve ser substituída por uma implementação que consulte um banco de dados
 * ou serviço de autenticação adequado.</p>
 */
@Configuration
public class InMemoryUserConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return username -> {
            if ("admin".equals(username)) {
                return User.withUsername("admin")
                        .password(encoder.encode("jurispay@admin"))
                        .roles("ADMIN")
                        .build();
            }
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        };
    }
}

