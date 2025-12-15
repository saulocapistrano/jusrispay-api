package br.com.jurispay.infrastructure.config.creditanalysis;

import br.com.jurispay.domain.creditanalysis.specification.DefaultDocumentChecklistSpecification;
import br.com.jurispay.domain.creditanalysis.specification.DocumentChecklistSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans do slice CreditAnalysis.
 * Expõe specifications de domínio como beans Spring sem adicionar anotações Spring no domínio.
 */
@Configuration
public class CreditAnalysisSpecificationConfig {

    /**
     * Bean para DocumentChecklistSpecification padrão.
     * Permite injeção via Spring sem adicionar anotações no domínio.
     *
     * @return instância de DefaultDocumentChecklistSpecification
     */
    @Bean
    public DocumentChecklistSpecification documentChecklistSpecification() {
        return new DefaultDocumentChecklistSpecification();
    }
}

