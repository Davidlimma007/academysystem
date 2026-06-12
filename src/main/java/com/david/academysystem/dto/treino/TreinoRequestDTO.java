package com.david.academysystem.dto.treino;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "Dados para criação ou atualização de treino")
public record TreinoRequestDTO(
        @Schema(description = "ID do aluno dono do treino", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "O aluno deve ser informado.")
        UUID alunoId,

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        @NotBlank(message = "O nome do treino deve ser informado.")
        String nome,

        @Schema(description = "Lista de IDs dos exercícios incluídos no treino", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
        @NotEmpty(message = "Os exercícios do aluno devem ser informados.")
        List<UUID> exerciciosIds
) {
}
