package com.david.academysystem.dto.auth.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public record LoginRequestDTO(
        @Schema(description = "E-mail do usuário", example = "joao@email.com")
        @NotBlank(message = "O e-mail deve ser informado.")
        String email,

        @Schema(description = "Senha de acesso", example = "senha@123")
        @NotBlank(message = "A senha deve ser informada.")
        String senha
) {
}
