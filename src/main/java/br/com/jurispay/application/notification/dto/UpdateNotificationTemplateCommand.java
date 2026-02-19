package br.com.jurispay.application.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateNotificationTemplateCommand {

    private String code;
    private String content;
    private Boolean active;
}
