package br.com.jurispay.api.dto.auth;

import java.util.List;

public record MeResponse(
        String username,
        List<String> roles
) {
}
