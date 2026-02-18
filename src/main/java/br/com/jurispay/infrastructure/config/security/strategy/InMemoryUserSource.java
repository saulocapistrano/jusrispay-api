package br.com.jurispay.infrastructure.config.security.strategy;

import br.com.jurispay.infrastructure.config.security.model.DevUserDefinition;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação Strategy para usuários em memória.
 * Usa um Map para lookup rápido, sem ifs.
 * 
 * <p><strong>ATENÇÃO:</strong> Apenas para ambiente de desenvolvimento.
 * Não deve estar ativo em produção.</p>
 */
@Component
@Profile({"dev", "docker"})
public class InMemoryUserSource implements UserSource {

    private final Map<String, DevUserDefinition> users;

    /**
     * Construtor que inicializa o Map com os usuários de desenvolvimento.
     */
    public InMemoryUserSource() {
        this.users = Map.of(
            "admin", DevUserDefinition.builder()
                .username("admin")
                .rawPassword("admin123")
                .roles(List.of("ADMIN"))
                .build(),
            "operator", DevUserDefinition.builder()
                .username("operator")
                .rawPassword("operator123")
                .roles(List.of("OPERATOR"))
                .build(),
            "auditor", DevUserDefinition.builder()
                .username("auditor")
                .rawPassword("auditor123")
                .roles(List.of("AUDITOR"))
                .build()
        );
    }

    @Override
    public Optional<DevUserDefinition> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}

