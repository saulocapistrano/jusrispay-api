package br.com.jurispay.application.notification.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class NotificationTemplateRenderer {

    private static final Set<String> ALLOWED = Set.of(
            "customer_name",
            "due_date",
            "amount",
            "pix_key",
            "installment_number",
            "total_installments",
            "days_overdue"
    );

    public String render(String template, Map<String, String> values) {
        if (template == null) {
            throw new IllegalArgumentException("Template não pode ser nulo.");
        }
        if (values == null) {
            throw new IllegalArgumentException("Values não pode ser nulo.");
        }

        validateTemplate(template);

        String rendered = template;
        for (var e : values.entrySet()) {
            if (!ALLOWED.contains(e.getKey())) {
                throw new IllegalArgumentException("Placeholder não permitido: " + e.getKey());
            }
            String placeholder = "{{" + e.getKey() + "}}";
            rendered = rendered.replace(placeholder, e.getValue() == null ? "" : e.getValue());
        }

        if (rendered.contains("{{") || rendered.contains("}}")) {
            throw new IllegalArgumentException("Template possui placeholders não resolvidos.");
        }
        return rendered;
    }

    public void validateTemplate(String template) {
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("Template não pode ser vazio.");
        }
        int idx = 0;
        while (true) {
            int start = template.indexOf("{{", idx);
            if (start < 0) {
                return;
            }
            int end = template.indexOf("}}", start + 2);
            if (end < 0) {
                throw new IllegalArgumentException("Template possui placeholder não fechado.");
            }
            String key = template.substring(start + 2, end).trim();
            if (!ALLOWED.contains(key)) {
                throw new IllegalArgumentException("Placeholder não permitido: " + key);
            }
            idx = end + 2;
        }
    }
}
