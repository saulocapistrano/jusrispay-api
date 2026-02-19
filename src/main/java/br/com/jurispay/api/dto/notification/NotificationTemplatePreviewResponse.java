package br.com.jurispay.api.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationTemplatePreviewResponse {

    private String renderedMessage;
}
