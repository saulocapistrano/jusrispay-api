package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.infrastructure.contract.pdf.ContractPdfRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * Testes para GenerateContractPdfUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GenerateContractPdfUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private ContractPdfRenderer contractPdfRenderer;

    @InjectMocks
    private GenerateContractPdfUseCaseImpl useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: loanId não existe no repositório
        // When: gerar PDF do contrato
        // Then: lançar NotFoundException("Empréstimo não encontrado.")
    }

    @Test
    void shouldGeneratePdfAndSaveDocument() {
        // TODO: Implementar teste de cenário feliz
        // Given: empréstimo existe, PDF renderizado
        // When: gerar PDF do contrato
        // Then:
        //   - fileStorageRepository.put foi chamado
        //   - documentRepository.save foi chamado
        //   - Document salvo tem type = CONTRACT_PDF
        //   - Document salvo tem status = VALIDATED
        //   - Document salvo tem loanId preenchido
    }
}

