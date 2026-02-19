package br.com.jurispay.api.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotificationTemplatePreviewRequest {

    private Map<String, String> values;
}
