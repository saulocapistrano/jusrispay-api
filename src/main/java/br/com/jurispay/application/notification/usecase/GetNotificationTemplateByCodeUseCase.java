package br.com.jurispay.application.notification.usecase;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;

public interface GetNotificationTemplateByCodeUseCase {

    NotificationTemplateResponse getByCode(String code);
}
