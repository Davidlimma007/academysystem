package com.david.academysystem.dto.treino;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

@Schema(description = "Dados para criação de treino pelo próprio aluno")
public record TreinoAlunoRequestDTO(
        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        @NotBlank(message = "O nome do treino deve ser informado.")
        String nome,

        @Schema(description = "Lista de IDs dos exercícios incluídos no treino")
        @NotEmpty(message = "Os exercícios do treino devem ser informados.")
        List<UUID> exerciciosIds
) {
}
