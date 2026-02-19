package br.com.jurispay.infrastructure.notification.provider.meta;

public record MetaSendTextResponse(
        Message[] messages
) {
    public record Message(String id) {
    }
}
