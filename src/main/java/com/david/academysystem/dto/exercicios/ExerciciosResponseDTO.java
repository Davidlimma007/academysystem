package com.david.academysystem.dto.exercicios;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do exercício retornados pela API")
public record ExerciciosResponseDTO(
        @Schema(description = "ID do exercício", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do exercício", example = "Supino reto")
        String nome,

        @Schema(description = "Grupo muscular trabalhado", example = "Peito")
        String grupoMuscular
) {
}
