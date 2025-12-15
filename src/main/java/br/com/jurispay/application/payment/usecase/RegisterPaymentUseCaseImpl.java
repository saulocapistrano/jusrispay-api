package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.application.payment.validator.PaymentRegistrationCommandValidator;
import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.loan.service.RoiCalculator;
import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final OverdueCalculator overdueCalculator;
    private final RoiCalculator roiCalculator;
    private final PaymentRegistrationCommandValidator validator;

    public RegisterPaymentUseCaseImpl(
            PaymentRepository paymentRepository,
            LoanRepository loanRepository,
            PaymentApplicationMapper mapper,
            OverdueCalculator overdueCalculator,
            RoiCalculator roiCalculator,
            PaymentRegistrationCommandValidator validator) {
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
        this.overdueCalculator = overdueCalculator;
        this.roiCalculator = roiCalculator;
        this.validator = validator;
    }

    @Override
    public PaymentResponse register(PaymentRegistrationCommand command) {
        // Validações básicas
        validator.validate(command);

        // Buscar empréstimo
        Loan loan = loanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado para registrar pagamento."));

        // Definir dataPagamento
        Instant dataPagamento = command.getDataPagamento() != null
                ? command.getDataPagamento()
                : Instant.now();

        // Calcular multa usando OverdueCalculator
        BigDecimal multaDiaria = loan.getMultaDiaria() != null
                ? loan.getMultaDiaria()
                : MULTA_DIARIA_PADRAO;
        OverdueInfo overdueInfo = overdueCalculator.calculate(
                loan.getDataPrevistaDevolucao(),
                dataPagamento,
                multaDiaria
        );

        // Valor final recebido
        BigDecimal valorFinalRecebido = command.getValorPago();

        // Calcular ROI usando RoiCalculator
        BigDecimal roiBrl = roiCalculator.calculateRoiBrl(valorFinalRecebido, loan.getValorSolicitado());
        BigDecimal roiPercent = roiCalculator.calculateRoiPercent(roiBrl, loan.getValorSolicitado());

        // Criar Payment base
        Payment payment = mapper.toDomain(command);

        // Construir Payment completo
        Payment paymentCompleto = Payment.builder()
                .loanId(payment.getLoanId())
                .valorPago(payment.getValorPago())
                .dataPagamento(dataPagamento)
                .metodo(payment.getMetodo())
                .diasAtraso((int) overdueInfo.getDaysOverdue())
                .multaTotal(overdueInfo.getTotalFine())
                .valorFinalRecebido(valorFinalRecebido)
                .roiBrl(roiBrl)
                .roiPercent(roiPercent)
                .dataCriacao(Instant.now())
                .build();

        // Salvar pagamento
        Payment savedPayment = paymentRepository.save(paymentCompleto);

        // Atualizar Loan para PAID usando método do domínio
        Loan paidLoan = loan.markAsPaid();
        loanRepository.save(paidLoan);

        // Retornar resposta
        return mapper.toResponse(savedPayment);
    }
}

