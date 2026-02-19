package br.com.jurispay.application.notification.service;

import org.springframework.stereotype.Component;

@Component
public class NotificationPhoneResolver {

    public String resolveToPhone(String customerCellphone, String customerPhone) {
        String cell = normalizeToE164(customerCellphone);
        if (cell != null) {
            return cell;
        }
        String phone = normalizeToE164(customerPhone);
        if (phone != null) {
            return phone;
        }
        return null;
    }

    private String normalizeToE164(String raw) {
        if (raw == null) {
            return null;
        }
        String digits = raw.replaceAll("\\D", "");
        if (digits.isBlank()) {
            return null;
        }

        if (digits.startsWith("00")) {
            digits = digits.substring(2);
        }

        if (!digits.startsWith("55") && (digits.length() == 10 || digits.length() == 11)) {
            digits = "55" + digits;
        }

        if (!digits.startsWith("55")) {
            return null;
        }

        if (digits.length() < 12 || digits.length() > 13) {
            return null;
        }

        return "+" + digits;
    }
}
