package br.com.jurispay.api.controller.notification;

import br.com.jurispay.application.notification.dto.NotificationOutboxResponse;
import br.com.jurispay.application.notification.usecase.ListNotificationOutboxUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/outbox")
public class NotificationOutboxController {

    private final ListNotificationOutboxUseCase listUseCase;

    public NotificationOutboxController(ListNotificationOutboxUseCase listUseCase) {
        this.listUseCase = listUseCase;
    }

    @GetMapping
    public ResponseEntity<List<NotificationOutboxResponse>> listTop(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long loanId,
            @RequestParam(required = false) Long receivableId
    ) {
        return ResponseEntity.ok(listUseCase.listTop(status, customerId, loanId, receivableId));
    }
}
