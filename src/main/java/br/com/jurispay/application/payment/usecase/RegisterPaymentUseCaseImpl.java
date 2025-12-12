package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

/**
 * Implementação do caso de uso de registro de pagamento.
 * Calcula atraso, multa e ROI, e atualiza o status do empréstimo para PAID.
 */
@Service
public class RegisterPaymentUseCaseImpl implements RegisterPaymentUseCase {

    private static final BigDecimal MULTA_DIARIA_PADRAO = new BigDecimal("20.00");

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final PaymentApplicationMapper mapper;

    public RegisterPaymentUseCaseImpl(
            PaymentRepository paymentRepository,
            LoanRepository loanRepository,
            PaymentApplicationMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentResponse register(PaymentRegistrationCommand command) {
        // Validações básicas
        validateCommand(command);

        // Buscar empréstimo
        Loan loan = loanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado para registrar pagamento."));

        // Definir dataPagamento
        Instant dataPagamento = command.getDataPagamento() != null
                ? command.getDataPagamento()
                : Instant.now();

        // Calcular dias de atraso
        int diasAtraso = calculateDiasAtraso(dataPagamento, loan.getDataPrevistaDevolucao());

        // Calcular multa total
        BigDecimal multaDiaria = loan.getMultaDiaria() != null
                ? loan.getMultaDiaria()
                : MULTA_DIARIA_PADRAO;
        BigDecimal multaTotal = multaDiaria.multiply(BigDecimal.valueOf(diasAtraso));

        // Valor final recebido
        BigDecimal valorFinalRecebido = command.getValorPago();

        // Calcular ROI
        BigDecimal roiBrl = valorFinalRecebido.subtract(loan.getValorSolicitado());
        BigDecimal roiPercent = calculateRoiPercent(roiBrl, loan.getValorSolicitado());

        // Criar Payment base
        Payment payment = mapper.toDomain(command);

        // Construir Payment completo
        Payment paymentCompleto = Payment.builder()
                .loanId(payment.getLoanId())
                .valorPago(payment.getValorPago())
                .dataPagamento(dataPagamento)
                .metodo(payment.getMetodo())
                .diasAtraso(diasAtraso)
                .multaTotal(multaTotal)
                .valorFinalRecebido(valorFinalRecebido)
                .roiBrl(roiBrl)
                .roiPercent(roiPercent)
                .dataCriacao(Instant.now())
                .build();

        // Salvar pagamento
        Payment savedPayment = paymentRepository.save(paymentCompleto);

        // Atualizar Loan para PAID
        updateLoanToPaid(loan);

        // Retornar resposta
        return mapper.toResponse(savedPayment);
    }

    private void validateCommand(PaymentRegistrationCommand command) {
        if (command.getLoanId() == null) {
            throw new ValidationException("ID do empréstimo é obrigatório.");
        }

        if (command.getValorPago() == null || command.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Valor pago deve ser maior que zero.");
        }
    }

    private int calculateDiasAtraso(Instant dataPagamento, Instant dataPrevistaDevolucao) {
        if (dataPrevistaDevolucao == null) {
            return 0;
        }

        if (dataPagamento.isBefore(dataPrevistaDevolucao)) {
            return 0;
        }

        Duration duration = Duration.between(dataPrevistaDevolucao, dataPagamento);
        return (int) duration.toDays();
    }

    private BigDecimal calculateRoiPercent(BigDecimal roiBrl, BigDecimal valorSolicitado) {
        if (valorSolicitado == null || valorSolicitado.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return roiBrl
                .divide(valorSolicitado, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void updateLoanToPaid(Loan loan) {
        Loan loanAtualizado = Loan.builder()
                .id(loan.getId())
                .customerId(loan.getCustomerId())
                .valorSolicitado(loan.getValorSolicitado())
                .valorDevolucaoPrevista(loan.getValorDevolucaoPrevista())
                .taxaJuros(loan.getTaxaJuros())
                .multaDiaria(loan.getMultaDiaria())
                .dataLiberacao(loan.getDataLiberacao())
                .dataPrevistaDevolucao(loan.getDataPrevistaDevolucao())
                .dataCriacao(loan.getDataCriacao())
                .dataAtualizacao(Instant.now())
                .status(LoanStatus.PAID)
                .build();

        loanRepository.save(loanAtualizado);
    }
}

