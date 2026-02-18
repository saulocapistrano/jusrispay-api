package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Implementação do caso de uso de registro de cliente.
 */
@Service
public class RegisterCustomerUseCaseImpl implements RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterCustomerUseCaseImpl(
            CustomerRepository customerRepository,
            CustomerApplicationMapper mapper,
            PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerResponse register(CustomerRegistrationCommand command) {
        // Valida se CPF já existe
        if (customerRepository.existsByCpf(command.getCpf())) {
            throw new ValidationException(ErrorCode.DUPLICATE_CPF, "CPF já cadastrado.");
        }

        // Converte comando para domínio
        Customer customer = mapper.toDomain(command);

        Customer customerToSave;
        if (command.getSenha() != null && !command.getSenha().isBlank()) {
            String senhaHash = passwordEncoder.encode(command.getSenha());
            customerToSave = Customer.builder()
                    .id(customer.getId())
                    .nomeCompleto(customer.getNomeCompleto())
                    .idade(customer.getIdade())
                    .cpf(customer.getCpf())
                    .rg(customer.getRg())
                    .dataNasc(customer.getDataNasc())
                    .sexo(customer.getSexo())
                    .signo(customer.getSigno())
                    .mae(customer.getMae())
                    .pai(customer.getPai())
                    .email(customer.getEmail())
                    .senhaHash(senhaHash)
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
                    .chavePix(customer.getChavePix())
                    .rendaMensal(customer.getRendaMensal())
                    .ocupacaoAtual(customer.getOcupacaoAtual())
                    .redesSociais(customer.getRedesSociais())
                    .dataCriacao(customer.getDataCriacao())
                    .dataAtualizacao(customer.getDataAtualizacao())
                    .build();
        } else {
            customerToSave = customer;
        }

        // Salva cliente
        Customer savedCustomer = customerRepository.save(customerToSave);

        // Retorna resposta mascarada
        return mapper.toResponse(savedCustomer);
    }
}

