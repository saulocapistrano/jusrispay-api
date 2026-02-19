package br.com.jurispay.application.notification.usecase;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.application.notification.dto.UpdateNotificationTemplateCommand;

public interface UpdateNotificationTemplateUseCase {

    NotificationTemplateResponse update(UpdateNotificationTemplateCommand command);
}
