package com.david.academysystem.dto.treino;

import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Dados do treino retornados pela API")
public record TreinoResponseDTO(
        @Schema(description = "ID do treino", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        String nome,

        @Schema(description = "ID do aluno dono do treino", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID alunoId,

        @Schema(description = "Exercícios que compõem o treino")
        List<ExerciciosResponseDTO> exercicios
) {
}
