package br.com.jurispay.infrastructure.notification.provider.meta;

import br.com.jurispay.application.notification.port.WhatsAppSendResult;
import br.com.jurispay.application.notification.port.WhatsAppSenderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MetaWhatsAppCloudSenderAdapter implements WhatsAppSenderPort {

    private final RestClient restClient;
    private final String accessToken;
    private final String phoneNumberId;
    private final String apiVersion;

    public MetaWhatsAppCloudSenderAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${WHATSAPP_META_ACCESS_TOKEN:}") String accessToken,
            @Value("${WHATSAPP_META_PHONE_NUMBER_ID:}") String phoneNumberId,
            @Value("${WHATSAPP_META_API_VERSION:v20.0}") String apiVersion
    ) {
        this.restClient = restClientBuilder
                .baseUrl("https://graph.facebook.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.accessToken = resolve("WHATSAPP_META_ACCESS_TOKEN", accessToken);
        this.phoneNumberId = resolve("WHATSAPP_META_PHONE_NUMBER_ID", phoneNumberId);
        this.apiVersion = resolve("WHATSAPP_META_API_VERSION", apiVersion);
    }

    @Override
    public WhatsAppSendResult sendText(String toPhoneE164, String message) {
        if (accessToken == null) {
            throw new IllegalStateException("WHATSAPP_META_ACCESS_TOKEN não configurado.");
        }
        if (phoneNumberId == null) {
            throw new IllegalStateException("WHATSAPP_META_PHONE_NUMBER_ID não configurado.");
        }
        if (toPhoneE164 == null || toPhoneE164.isBlank()) {
            throw new IllegalArgumentException("Telefone destino inválido.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Mensagem não pode ser vazia.");
        }

        String path = "/" + apiVersion + "/" + phoneNumberId + "/messages";

        var request = new MetaSendTextRequest(
                "whatsapp",
                toPhoneE164.replace("+", ""),
                "text",
                new MetaSendTextRequest.TextBody(message)
        );

        var response = restClient.post()
                .uri(path)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(request)
                .retrieve()
                .body(MetaSendTextResponse.class);

        if (response == null || response.messages() == null || response.messages().length == 0) {
            throw new IllegalStateException("Resposta inválida do provider.");
        }

        return new WhatsAppSendResult(response.messages()[0].id());
    }

    private String resolve(String envKey, String injectedValue) {
        String fromInjection = normalize(injectedValue);
        if (fromInjection != null) {
            return fromInjection;
        }
        return normalize(System.getenv(envKey));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim();
        return v.isBlank() ? null : v;
    }
}
