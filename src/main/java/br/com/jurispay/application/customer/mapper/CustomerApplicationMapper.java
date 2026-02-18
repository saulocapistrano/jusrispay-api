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
                .idade(command.getIdade())
                .cpf(command.getCpf())
                .rg(command.getRg())
                .dataNasc(command.getDataNasc())
                .sexo(command.getSexo())
                .signo(command.getSigno())
                .mae(command.getMae())
                .pai(command.getPai())
                .email(command.getEmail())
                .cep(command.getCep())
                .endereco(command.getEndereco())
                .numero(command.getNumero())
                .bairro(command.getBairro())
                .cidade(command.getCidade())
                .estado(command.getEstado())
                .telefoneFixo(command.getTelefoneFixo())
                .celular(command.getCelular())
                .bemGarantidor(command.getBemGarantidor())
                .descricaoBem(command.getDescricaoBem())
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
                .idade(customer.getIdade())
                .cpfMascarado(SensitiveDataMasker.maskCpf(customer.getCpf()))
                .rg(customer.getRg())
                .dataNasc(customer.getDataNasc())
                .sexo(customer.getSexo())
                .signo(customer.getSigno())
                .mae(customer.getMae())
                .pai(customer.getPai())
                .email(customer.getEmail())
                .cep(customer.getCep())
                .endereco(customer.getEndereco())
                .numero(customer.getNumero())
                .bairro(customer.getBairro())
                .cidade(customer.getCidade())
                .estado(customer.getEstado())
                .telefoneFixo(customer.getTelefoneFixo())
                .celular(customer.getCelular())
                .bemGarantidor(customer.getBemGarantidor())
                .descricaoBem(customer.getDescricaoBem())
                .telefone(customer.getTelefone())
                .chavePixMascarada(SensitiveDataMasker.maskPixKey(customer.getChavePix()))
                .rendaMensal(customer.getRendaMensal())
                .ocupacaoAtual(customer.getOcupacaoAtual())
                .redesSociais(customer.getRedesSociais())
                .dataCriacao(customer.getDataCriacao())
                .build();
    }
}

