package br.com.jurispay.domain.customer.repository;

import br.com.jurispay.domain.customer.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do domínio Customer.
 * Define as operações de persistência sem depender de implementação específica.
 */
public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Customer> findAll();
}

