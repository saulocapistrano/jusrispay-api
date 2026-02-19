package br.com.jurispay.infrastructure.notification.provider.meta;

public record MetaSendTextRequest(
        String messaging_product,
        String to,
        String type,
        TextBody text
) {
    public record TextBody(String body) {
    }
}
