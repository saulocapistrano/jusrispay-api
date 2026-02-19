package br.com.jurispay.api.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNotificationTemplateRequest {

    @NotBlank
    @Size(max = 2000)
    private String content;

    @NotNull
    private Boolean active;
}
