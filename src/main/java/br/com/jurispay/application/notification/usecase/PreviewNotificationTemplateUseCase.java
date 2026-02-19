package br.com.jurispay.application.notification.usecase;

import java.util.Map;

public interface PreviewNotificationTemplateUseCase {

    String preview(String code, Map<String, String> values);
}
