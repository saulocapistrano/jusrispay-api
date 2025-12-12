package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CustomerEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio.
 */
@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    CustomerEntity toEntity(Customer customer);

    Customer toDomain(CustomerEntity entity);
}

