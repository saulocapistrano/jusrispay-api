package br.com.jurispay.application.notification.usecase;

import br.com.jurispay.application.notification.dto.NotificationOutboxResponse;

import java.util.List;

public interface ListNotificationOutboxUseCase {

    List<NotificationOutboxResponse> listTop(
            String status,
            Long customerId,
            Long loanId,
            Long receivableId
    );
}
