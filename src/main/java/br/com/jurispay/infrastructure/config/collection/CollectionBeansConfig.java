package br.com.jurispay.infrastructure.config.collection;

import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.loan.service.RoiCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans relacionados a collection e loan.
 * Fornece beans do domínio para injeção Spring sem adicionar anotações no domínio.
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

    /**
     * Bean para RoiCalculator do domínio.
     * Permite injeção via Spring sem adicionar anotações no domínio.
     *
     * @return instância de RoiCalculator
     */
    @Bean
    public RoiCalculator roiCalculator() {
        return new RoiCalculator();
    }
}

