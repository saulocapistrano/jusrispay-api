package br.com.jurispay.application.loan.assembler;

import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanResponseAssembler {

    private final CustomerRepository customerRepository;

    public LoanResponseAssembler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public LoanResponse toResponse(Loan loan) {
        String customerName = customerRepository.findById(loan.getCustomerId())
                .map(Customer::getNomeCompleto)
                .orElse(null);

        return LoanResponse.builder()
                .id(loan.getId())
                .customerId(loan.getCustomerId())
                .customerName(customerName)
                .valorSolicitado(loan.getValorSolicitado())
                .valorDevolucaoPrevista(loan.getValorDevolucaoPrevista())
                .taxaJuros(loan.getTaxaJuros())
                .multaDiaria(loan.getMultaDiaria())
                .periodoPagamento(loan.getPeriodoPagamento())
                .quantidadeParcelas(loan.getQuantidadeParcelas())
                .valorParcela(loan.getValorParcela())
                .status(loan.getStatus())
                .dataLiberacao(loan.getDataLiberacao())
                .dataPrevistaDevolucao(loan.getDataPrevistaDevolucao())
                .dataCriacao(loan.getDataCriacao())
                .build();
    }
}
