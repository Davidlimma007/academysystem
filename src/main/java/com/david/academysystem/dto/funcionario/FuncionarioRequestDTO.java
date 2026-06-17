package com.david.academysystem.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para criação ou atualização do perfil de funcionário")
public record FuncionarioRequestDTO(
        @Schema(description = "ID do usuário retornado no cadastro em /v1/auth/register/funcionario", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "O usuário deve ser informado.")
        UUID usuarioId,

        @Schema(description = "Nome completo do funcionário", example = "Maria Souza")
        @NotBlank(message = "O nome do funcionário deve ser informado.")
        String nome
) {
}
