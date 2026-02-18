package br.com.jurispay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Controller para a raiz da aplicação.
 * Redireciona para o Swagger UI.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Void> home() {
        return ResponseEntity.status(302)
                .location(URI.create("/swagger-ui/index.html"))
                .build();
    }
}

