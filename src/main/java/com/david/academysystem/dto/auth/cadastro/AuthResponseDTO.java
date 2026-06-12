package com.david.academysystem.dto.auth.cadastro;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Retorno após o cadastro de um novo usuário")
public record AuthResponseDTO(
        @Schema(description = "ID gerado para o usuário — use-o para criar o perfil (aluno/funcionário)", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "E-mail do usuário cadastrado", example = "joao@email.com")
        String email
) {
}
