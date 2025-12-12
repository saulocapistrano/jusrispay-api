package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Implementação do use case de início de análise de crédito.
 */
@Service
public class StartCreditAnalysisUseCaseImpl implements StartCreditAnalysisUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CustomerRepository customerRepository;
    private final CreditAnalysisApplicationMapper mapper;

    public StartCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CustomerRepository customerRepository,
            CreditAnalysisApplicationMapper mapper) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditAnalysisResponse start(StartCreditAnalysisCommand command) {
        // Validações básicas
        if (command.getCustomerId() == null) {
            throw new ValidationException("ID do cliente é obrigatório.");
        }

        if (command.getAnalystUserId() == null) {
            throw new ValidationException("ID do analista é obrigatório.");
        }

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado para iniciar análise."));

        // Verificar se já existe análise para este cliente
        CreditAnalysis existingAnalysis = creditAnalysisRepository.findByCustomerId(command.getCustomerId())
                .orElse(null);

        CreditAnalysis analysis;

        if (existingAnalysis != null) {
            CreditAnalysisStatus currentStatus = existingAnalysis.getStatus();

            if (currentStatus == CreditAnalysisStatus.APPROVED) {
                throw new ValidationException("Cliente já aprovado.");
            }

            if (currentStatus == CreditAnalysisStatus.IN_REVIEW) {
                throw new ValidationException("Análise já está em andamento.");
            }

            // Se REJECTED ou PENDING, permitir reiniciar
            Instant now = Instant.now();
            analysis = CreditAnalysis.builder()
                    .id(existingAnalysis.getId())
                    .customerId(existingAnalysis.getCustomerId())
                    .status(CreditAnalysisStatus.IN_REVIEW)
                    .analystUserId(command.getAnalystUserId())
                    .startedAt(now)
                    .decisionDeadlineAt(now.plus(24, ChronoUnit.HOURS))
                    .rejectionReason(null)
                    .notes(null)
                    .createdAt(existingAnalysis.getCreatedAt())
                    .updatedAt(now)
                    .build();
        } else {
            // Criar nova análise
            Instant now = Instant.now();
            analysis = CreditAnalysis.builder()
                    .customerId(command.getCustomerId())
                    .status(CreditAnalysisStatus.IN_REVIEW)
                    .analystUserId(command.getAnalystUserId())
                    .startedAt(now)
                    .decisionDeadlineAt(now.plus(24, ChronoUnit.HOURS))
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
        }

        // Salvar análise
        CreditAnalysis savedAnalysis = creditAnalysisRepository.save(analysis);

        // Retornar response
        return mapper.toResponse(savedAnalysis);
    }
}

