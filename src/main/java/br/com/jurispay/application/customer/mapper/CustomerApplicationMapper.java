package br.com.jurispay.application.customer.mapper;

import br.com.jurispay.application.common.util.SensitiveDataMasker;
import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.domain.customer.model.Customer;
import org.mapstruct.Mapper;

import java.time.Instant;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio.
 * Utiliza SensitiveDataMasker para mascarar dados sensíveis.
 */
@Mapper(componentModel = "spring")
public interface CustomerApplicationMapper {

    default Customer toDomain(CustomerRegistrationCommand command) {
        if (command == null) {
            return null;
        }

        Instant now = Instant.now();
        return Customer.builder()
                .nomeCompleto(command.getNomeCompleto())
                .cpf(command.getCpf())
                .telefone(command.getTelefone())
                .chavePix(command.getChavePix())
                .rendaMensal(command.getRendaMensal())
                .ocupacaoAtual(command.getOcupacaoAtual())
                .redesSociais(command.getRedesSociais())
                .dataCriacao(now)
                .dataAtualizacao(now)
                .build();
    }

    default CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .nomeCompleto(customer.getNomeCompleto())
                .cpfMascarado(SensitiveDataMasker.maskCpf(customer.getCpf()))
                .telefone(customer.getTelefone())
                .chavePixMascarada(SensitiveDataMasker.maskPixKey(customer.getChavePix()))
                .rendaMensal(customer.getRendaMensal())
                .ocupacaoAtual(customer.getOcupacaoAtual())
                .redesSociais(customer.getRedesSociais())
                .dataCriacao(customer.getDataCriacao())
                .build();
    }
}

