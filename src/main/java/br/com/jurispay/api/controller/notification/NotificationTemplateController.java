package br.com.jurispay.api.controller.notification;

import br.com.jurispay.api.dto.notification.NotificationTemplatePreviewRequest;
import br.com.jurispay.api.dto.notification.NotificationTemplatePreviewResponse;
import br.com.jurispay.api.dto.notification.UpdateNotificationTemplateRequest;
import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.application.notification.dto.UpdateNotificationTemplateCommand;
import br.com.jurispay.application.notification.usecase.GetNotificationTemplateByCodeUseCase;
import br.com.jurispay.application.notification.usecase.ListNotificationTemplatesUseCase;
import br.com.jurispay.application.notification.usecase.PreviewNotificationTemplateUseCase;
import br.com.jurispay.application.notification.usecase.UpdateNotificationTemplateUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/templates")
public class NotificationTemplateController {

    private final ListNotificationTemplatesUseCase listUseCase;
    private final GetNotificationTemplateByCodeUseCase getByCodeUseCase;
    private final UpdateNotificationTemplateUseCase updateUseCase;
    private final PreviewNotificationTemplateUseCase previewUseCase;

    public NotificationTemplateController(
            ListNotificationTemplatesUseCase listUseCase,
            GetNotificationTemplateByCodeUseCase getByCodeUseCase,
            UpdateNotificationTemplateUseCase updateUseCase,
            PreviewNotificationTemplateUseCase previewUseCase
    ) {
        this.listUseCase = listUseCase;
        this.getByCodeUseCase = getByCodeUseCase;
        this.updateUseCase = updateUseCase;
        this.previewUseCase = previewUseCase;
    }

    @GetMapping
    public ResponseEntity<List<NotificationTemplateResponse>> listAll() {
        return ResponseEntity.ok(listUseCase.listAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<NotificationTemplateResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(getByCodeUseCase.getByCode(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<NotificationTemplateResponse> update(
            @PathVariable String code,
            @Valid @RequestBody UpdateNotificationTemplateRequest request
    ) {
        var command = UpdateNotificationTemplateCommand.builder()
                .code(code)
                .content(request.getContent())
                .active(request.getActive())
                .build();
        return ResponseEntity.ok(updateUseCase.update(command));
    }

    @PostMapping("/{code}/preview")
    public ResponseEntity<NotificationTemplatePreviewResponse> preview(
            @PathVariable String code,
            @RequestBody NotificationTemplatePreviewRequest request
    ) {
        String rendered = previewUseCase.preview(code, request == null ? null : request.getValues());
        return ResponseEntity.ok(new NotificationTemplatePreviewResponse(rendered));
    }
}
