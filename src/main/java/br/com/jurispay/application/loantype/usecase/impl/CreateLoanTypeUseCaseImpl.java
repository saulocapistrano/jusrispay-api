package br.com.jurispay.application.loantype.usecase.impl;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.application.loantype.assembler.LoanTypeResponseAssembler;
import br.com.jurispay.application.loantype.usecase.CreateLoanTypeUseCase;
import br.com.jurispay.application.loantype.validator.LoanTypeRequestValidator;
import br.com.jurispay.domain.loantype.exception.LoanTypeDomainException;
import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.model.LoanTypeScheduleType;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateLoanTypeUseCaseImpl implements CreateLoanTypeUseCase {

    private final LoanTypeRepository repository;
    private final LoanTypeResponseAssembler assembler;
    private final LoanTypeRequestValidator validator;

    public CreateLoanTypeUseCaseImpl(
            LoanTypeRepository repository,
            LoanTypeResponseAssembler assembler,
            LoanTypeRequestValidator validator) {
        this.repository = repository;
        this.assembler = assembler;
        this.validator = validator;
    }

    @Override
    public LoanTypeResponse create(LoanTypeRequest request) {
        validator.validate(request);

        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw LoanTypeDomainException.of("LOAN_TYPE_DUPLICATE_NAME", "Já existe um tipo de empréstimo com este nome.");
        }

        Instant now = Instant.now();
        LoanType loanType = LoanType.builder()
                .id(null)
                .nome(request.getNome().trim())
                .descricao(request.getDescricao())
                .intervaloPagamentoDias(request.getIntervaloPagamentoDias())
                .scheduleType(LoanTypeScheduleType.valueOf(request.getScheduleType().trim().toUpperCase()))
                .weeklyDayOfWeek(request.getWeeklyDayOfWeek())
                .ativo(Boolean.TRUE.equals(request.getAtivo()))
                .dataCriacao(now)
                .dataAtualizacao(now)
                .build();

        LoanType saved = repository.save(loanType);
        return assembler.toResponse(saved);
    }
}
