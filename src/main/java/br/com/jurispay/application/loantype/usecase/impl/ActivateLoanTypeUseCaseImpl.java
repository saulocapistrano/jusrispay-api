package br.com.jurispay.application.loantype.usecase.impl;

import br.com.jurispay.application.loantype.usecase.ActivateLoanTypeUseCase;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loantype.exception.LoanTypeDomainException;
import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ActivateLoanTypeUseCaseImpl implements ActivateLoanTypeUseCase {

    private final LoanTypeRepository repository;

    public ActivateLoanTypeUseCaseImpl(LoanTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void activate(Long id) {
        if (id == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "id é obrigatório.");
        }

        LoanType existing = repository.findById(id)
                .orElseThrow(() -> LoanTypeDomainException.of("LOAN_TYPE_NOT_FOUND", "Tipo de empréstimo não encontrado."));

        LoanType updated = LoanType.builder()
                .id(existing.getId())
                .nome(existing.getNome())
                .descricao(existing.getDescricao())
                .intervaloPagamentoDias(existing.getIntervaloPagamentoDias())
                .scheduleType(existing.getScheduleType())
                .weeklyDayOfWeek(existing.getWeeklyDayOfWeek())
                .ativo(true)
                .dataCriacao(existing.getDataCriacao())
                .dataAtualizacao(Instant.now())
                .build();

        repository.save(updated);
    }
}
