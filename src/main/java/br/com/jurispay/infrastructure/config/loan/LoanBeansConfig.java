package br.com.jurispay.infrastructure.config.loan;

import br.com.jurispay.domain.loan.factory.LoanFactory;
import br.com.jurispay.domain.loan.policy.DefaultLoanPolicy;
import br.com.jurispay.domain.loan.policy.LoanPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans do slice Loan.
 * Expõe serviços de domínio como beans Spring sem adicionar anotações Spring no domínio.
 */
@Configuration
public class LoanBeansConfig {

    /**
     * Bean para LoanPolicy padrão.
     * Permite injeção via Spring sem adicionar anotações no domínio.
     *
     * @return instância de DefaultLoanPolicy
     */
    @Bean
    public LoanPolicy loanPolicy() {
        return new DefaultLoanPolicy();
    }

    /**
     * Bean para LoanFactory do domínio.
     * Permite injeção via Spring sem adicionar anotações no domínio.
     *
     * @return instância de LoanFactory
     */
    @Bean
    public LoanFactory loanFactory() {
        return new LoanFactory();
    }
}

