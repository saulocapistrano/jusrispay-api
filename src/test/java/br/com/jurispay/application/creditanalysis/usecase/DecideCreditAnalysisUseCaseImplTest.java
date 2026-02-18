package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.application.creditanalysis.service.CreditAnalysisDecisionValidator;
import br.com.jurispay.application.creditcheck.usecase.GetLatestCreditCheckByLoanUseCase;
import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckUseCase;
import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.application.risk.usecase.AttachJurispayRiskAssessmentToLatestCreditCheckUseCase;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.creditdecisionoverride.repository.CreditDecisionOverrideRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.loan.service.InstallmentScheduleService;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para DecideCreditAnalysisUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class DecideCreditAnalysisUseCaseImplTest {

    @Mock
    private CreditAnalysisRepository creditAnalysisRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private CreditAnalysisDecisionValidator decisionValidator;

    @Mock
    private CustomerKycService customerKycService;

    @Mock
    private InstallmentScheduleService scheduleService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RunCreditCheckUseCase runCreditCheckUseCase;

    @Mock
    private GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase;

    @Mock
    private CreditDecisionOverrideRepository creditDecisionOverrideRepository;

    @Mock
    private AttachJurispayRiskAssessmentToLatestCreditCheckUseCase attachJurispayRiskAssessmentToLatestCreditCheckUseCase;

    @Mock
    private CreditAnalysisApplicationMapper mapper;

    @InjectMocks
    private DecideCreditAnalysisUseCaseImpl useCase;

    @Test
    void shouldThrowValidationExceptionWhenApproveWithIncompleteChecklist() {
        // TODO: Implementar teste de aprovação com checklist incompleto
        // Given: análise em IN_REVIEW, cliente não possui todos os documentos obrigatórios validados
        // When: tentar aprovar análise
        // Then: lançar ValidationException("Checklist de documentos incompleto para aprovação.")
    }

    @Test
    void shouldThrowValidationExceptionWhenRejectWithoutRejectionReason() {
        // TODO: Implementar teste de reprovação sem motivo
        // Given: análise em IN_REVIEW, decisão REJECTED sem rejectionReason
        // When: tentar reprovar análise
        // Then: lançar ValidationException("Motivo da reprovação é obrigatório.")
    }

    @Test
    void shouldDecideSuccessfullyWhenDecisionIsValid() {
        // TODO: Implementar teste de decisão OK
        // Given: análise em IN_REVIEW, decisão válida (APPROVED com checklist completo ou REJECTED com motivo)
        // When: decidir análise
        // Then: análise atualizada com status e finishedAt preenchidos, salva e retornada
    }
}

