package com.david.academysystem.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do funcionário retornados pela API")
public record FuncionarioResponseDTO(
        @Schema(description = "ID do funcionário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do funcionário", example = "Maria Souza")
        String nome,

        @Schema(description = "E-mail do usuário vinculado", example = "maria@email.com")
        String email
) {
}
