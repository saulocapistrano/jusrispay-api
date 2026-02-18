package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeEntityMapper {

    public LoanType toDomain(LoanTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        return LoanType.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .intervaloPagamentoDias(entity.getIntervaloPagamentoDias())
                .scheduleType(entity.getScheduleType() != null ? br.com.jurispay.domain.loantype.model.LoanTypeScheduleType.valueOf(entity.getScheduleType()) : null)
                .weeklyDayOfWeek(entity.getWeeklyDayOfWeek())
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }

    public LoanTypeEntity toEntity(LoanType domain) {
        if (domain == null) {
            return null;
        }
        return LoanTypeEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .intervaloPagamentoDias(domain.getIntervaloPagamentoDias())
                .scheduleType(domain.getScheduleType() != null ? domain.getScheduleType().name() : null)
                .weeklyDayOfWeek(domain.getWeeklyDayOfWeek())
                .ativo(domain.getAtivo())
                .dataCriacao(domain.getDataCriacao())
                .dataAtualizacao(domain.getDataAtualizacao())
                .build();
    }
}
