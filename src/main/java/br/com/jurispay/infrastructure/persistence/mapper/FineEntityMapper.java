package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.infrastructure.persistence.jpa.entity.FineEntity;
import org.springframework.stereotype.Component;

@Component
public class FineEntityMapper {

    public Fine toDomain(FineEntity entity) {
        if (entity == null) {
            return null;
        }

        return Fine.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .valor(entity.getValor())
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }

    public FineEntity toEntity(Fine fine) {
        if (fine == null) {
            return null;
        }

        return FineEntity.builder()
                .id(fine.getId())
                .nome(fine.getNome())
                .valor(fine.getValor())
                .ativo(fine.getAtivo())
                .dataCriacao(fine.getDataCriacao())
                .dataAtualizacao(fine.getDataAtualizacao())
                .build();
    }
}
