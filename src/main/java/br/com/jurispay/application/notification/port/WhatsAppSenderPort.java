package br.com.jurispay.application.notification.port;

public interface WhatsAppSenderPort {

    WhatsAppSendResult sendText(String toPhoneE164, String message);
}
