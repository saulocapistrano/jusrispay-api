package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.systemconfig.model.SystemConfig;
import br.com.jurispay.infrastructure.persistence.jpa.entity.SystemConfigEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemConfigEntityMapper {

    SystemConfigEntity toEntity(SystemConfig config);

    SystemConfig toDomain(SystemConfigEntity entity);
}
