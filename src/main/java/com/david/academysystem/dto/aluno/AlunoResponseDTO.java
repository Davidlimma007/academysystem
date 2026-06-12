package com.david.academysystem.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do aluno retornados pela API")
public record AlunoResponseDTO(
        @Schema(description = "ID do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do aluno", example = "João da Silva")
        String nome,

        @Schema(description = "E-mail do usuário vinculado", example = "joao@email.com")
        String email
) {
}
