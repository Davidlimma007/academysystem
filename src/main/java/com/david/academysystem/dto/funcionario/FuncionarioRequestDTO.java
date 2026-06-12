package com.david.academysystem.dto.funcionario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FuncionarioRequestDTO(
        @NotNull(message = "O usuário deve ser informado.")
        UUID usuarioId,
        @NotBlank(message = "O nome do funcionário deve ser informado.")
        String nome
) {
}
