package com.david.academysystem.dto.auth.cadastro;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank(message = "O e-mail deve ser informado.")
        String email,
        @NotBlank(message = "A senha deve ser informada.")
        String senha
) {
}
