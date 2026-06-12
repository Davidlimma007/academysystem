package com.david.academysystem.dto.auth.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O e-mail deve ser informado.")
        String email,
        @NotBlank(message = "A senha deve ser informada.")
        String senha
) {
}
