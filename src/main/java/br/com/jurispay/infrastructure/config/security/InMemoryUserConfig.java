package br.com.jurispay.infrastructure.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração de usuários em memória para ambiente de desenvolvimento.
 * 
 * <p><strong>ATENÇÃO:</strong> Esta configuração é apenas para ambiente de desenvolvimento.
 * Em produção, deve ser substituída por uma implementação que consulte um banco de dados
 * ou serviço de autenticação adequado.</p>
 * 
 * <p>Esta classe agora apenas marca o contexto como de desenvolvimento.
 * A lógica de usuários foi movida para {@link StrategyUserDetailsService} e {@link br.com.jurispay.infrastructure.config.security.strategy.InMemoryUserSource}.</p>
 */
@Configuration
@Profile({"dev", "docker"})
public class InMemoryUserConfig {
    // Configuração migrada para StrategyUserDetailsService + InMemoryUserSource
    // Mantida apenas para documentação e possível configuração futura
}

