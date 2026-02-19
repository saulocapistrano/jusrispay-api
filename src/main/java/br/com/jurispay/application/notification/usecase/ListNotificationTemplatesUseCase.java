package br.com.jurispay.application.notification.usecase;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;

import java.util.List;

public interface ListNotificationTemplatesUseCase {

    List<NotificationTemplateResponse> listAll();
}
