package br.com.jurispay.infrastructure.config.collection;

import br.com.jurispay.domain.collection.service.OverdueCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans relacionados a collection (cobrança).
 * Fornece beans do domínio para injeção Spring.
 */
@Configuration
public class CollectionBeansConfig {

    /**
     * Bean para OverdueCalculator do domínio.
     * Permite injeção via Spring sem adicionar anotações no domínio.
     *
     * @return instância de OverdueCalculator
     */
    @Bean
    public OverdueCalculator overdueCalculator() {
        return new OverdueCalculator();
    }
}

