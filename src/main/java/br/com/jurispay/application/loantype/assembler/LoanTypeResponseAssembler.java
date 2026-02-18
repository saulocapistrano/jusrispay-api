package br.com.jurispay.application.loantype.assembler;

import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.domain.loantype.model.LoanType;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeResponseAssembler {

    public LoanTypeResponse toResponse(LoanType loanType) {
        if (loanType == null) {
            return null;
        }
        return LoanTypeResponse.builder()
                .id(loanType.getId())
                .nome(loanType.getNome())
                .descricao(loanType.getDescricao())
                .intervaloPagamentoDias(loanType.getIntervaloPagamentoDias())
                .scheduleType(loanType.getScheduleType() != null ? loanType.getScheduleType().name() : null)
                .weeklyDayOfWeek(loanType.getWeeklyDayOfWeek())
                .ativo(loanType.getAtivo())
                .dataCriacao(loanType.getDataCriacao())
                .dataAtualizacao(loanType.getDataAtualizacao())
                .build();
    }
}
