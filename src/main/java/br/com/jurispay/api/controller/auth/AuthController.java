package br.com.jurispay.api.controller.auth;

import br.com.jurispay.api.dto.auth.LoginRequest;
import br.com.jurispay.api.dto.auth.TokenResponse;
import br.com.jurispay.application.auth.usecase.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para operações de autenticação.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    /**
     * Endpoint de login.
     * Autentica credenciais e retorna token JWT.
     *
     * @param request requisição com username e password
     * @return resposta com token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = loginUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

