package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para GetPaymentByIdUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GetPaymentByIdUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentApplicationMapper mapper;

    @InjectMocks
    private GetPaymentByIdUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        // Inicialização dos mocks já feita pelo MockitoExtension
    }

    @Test
    void shouldReturnPaymentResponseWhenPaymentExists() {
        // TODO: Implementar teste de pagamento encontrado
        // Given: ID existe no repositório
        // When: buscar por ID
        // Then: retornar PaymentResponse
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPaymentDoesNotExist() {
        // TODO: Implementar teste de pagamento não encontrado
        // Given: ID não existe no repositório
        // When: buscar por ID
        // Then: lançar NotFoundException com mensagem "Pagamento não encontrado."
    }
}

