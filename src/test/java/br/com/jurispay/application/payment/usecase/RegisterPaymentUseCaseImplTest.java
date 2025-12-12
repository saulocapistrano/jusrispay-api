package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para RegisterPaymentUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class RegisterPaymentUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private PaymentApplicationMapper mapper;

    @InjectMocks
    private RegisterPaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        // Inicialização dos mocks já feita pelo MockitoExtension
    }

    @Test
    void shouldRegisterPaymentWhenValidData() {
        // TODO: Implementar teste de cenário feliz
        // Given: comando válido, empréstimo existe
        // When: registrar pagamento
        // Then: pagamento salvo, empréstimo atualizado para PAID e retornado com sucesso
    }

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: loanId não existe no repositório
        // When: tentar registrar pagamento
        // Then: lançar NotFoundException com mensagem "Empréstimo não encontrado para registrar pagamento."
    }

    @Test
    void shouldThrowValidationExceptionWhenValorPagoIsInvalid() {
        // TODO: Implementar teste de validação de valor
        // Given: valorPago <= 0 ou null
        // When: tentar registrar pagamento
        // Then: lançar ValidationException com mensagem "Valor pago deve ser maior que zero."
    }
}

