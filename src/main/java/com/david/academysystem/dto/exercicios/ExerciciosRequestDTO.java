package com.david.academysystem.dto.exercicios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para criação ou atualização de exercício")
public record ExerciciosRequestDTO(
        @Schema(description = "Nome do exercício", example = "Supino reto")
        @NotBlank(message = "O nome do exercicio não pode ser vazio.")
        String nome,

        @Schema(description = "Grupo muscular trabalhado", example = "Peito")
        @NotBlank(message = "O nome do grupo muscular do exercicio não pode ser vazio.")
        String grupoMuscular
) {
}
