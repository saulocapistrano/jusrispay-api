package br.com.jurispay.application.loantype.usecase.impl;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.application.loantype.assembler.LoanTypeResponseAssembler;
import br.com.jurispay.application.loantype.usecase.UpdateLoanTypeUseCase;
import br.com.jurispay.application.loantype.validator.LoanTypeRequestValidator;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loantype.exception.LoanTypeDomainException;
import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.model.LoanTypeScheduleType;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateLoanTypeUseCaseImpl implements UpdateLoanTypeUseCase {

    private final LoanTypeRepository repository;
    private final LoanTypeResponseAssembler assembler;
    private final LoanTypeRequestValidator validator;

    public UpdateLoanTypeUseCaseImpl(
            LoanTypeRepository repository,
            LoanTypeResponseAssembler assembler,
            LoanTypeRequestValidator validator) {
        this.repository = repository;
        this.assembler = assembler;
        this.validator = validator;
    }

    @Override
    public LoanTypeResponse update(Long id, LoanTypeRequest request) {
        if (id == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "id é obrigatório.");
        }
        validator.validate(request);

        LoanType existing = repository.findById(id)
                .orElseThrow(() -> LoanTypeDomainException.of("LOAN_TYPE_NOT_FOUND", "Tipo de empréstimo não encontrado."));

        // Regra do catálogo pronto: não permitir editar os seeds semanais
        if (existing.getId() != null && (existing.getId() == 2L || existing.getId() == 7L)) {
            throw LoanTypeDomainException.of(
                    "LOAN_TYPE_CATALOG_IMMUTABLE",
                    "Tipos semanais do catálogo não podem ser editados; apenas ativar/desativar."
            );
        }

        Instant now = Instant.now();
        LoanType updated = LoanType.builder()
                .id(existing.getId())
                .nome(request.getNome().trim())
                .descricao(request.getDescricao())
                .intervaloPagamentoDias(request.getIntervaloPagamentoDias())
                .scheduleType(LoanTypeScheduleType.valueOf(request.getScheduleType().trim().toUpperCase()))
                .weeklyDayOfWeek(request.getWeeklyDayOfWeek())
                .ativo(Boolean.TRUE.equals(request.getAtivo()))
                .dataCriacao(existing.getDataCriacao())
                .dataAtualizacao(now)
                .build();

        LoanType saved = repository.save(updated);
        return assembler.toResponse(saved);
    }
}
