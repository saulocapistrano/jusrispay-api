package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CustomerEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCustomerRepository;
import br.com.jurispay.infrastructure.persistence.mapper.CustomerEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa CustomerRepository do domínio usando JPA.
 * Converte entre entidades JPA e modelos de domínio.
 */
@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final SpringDataCustomerRepository springDataRepository;
    private final CustomerEntityMapper mapper;

    public CustomerRepositoryAdapter(
            SpringDataCustomerRepository springDataRepository,
            CustomerEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity savedEntity = springDataRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        return springDataRepository.findByCpf(cpf)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return springDataRepository.existsByCpf(cpf);
    }

    @Override
    public List<Customer> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

