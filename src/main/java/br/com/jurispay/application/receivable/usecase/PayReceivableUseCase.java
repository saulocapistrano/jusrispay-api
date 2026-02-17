package br.com.jurispay.application.receivable.usecase;

import br.com.jurispay.application.receivable.dto.PayReceivableCommand;
import br.com.jurispay.application.receivable.dto.ReceivableResponse;

public interface PayReceivableUseCase {

    ReceivableResponse pay(PayReceivableCommand command);
}
