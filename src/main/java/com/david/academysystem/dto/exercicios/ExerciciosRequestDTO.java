package com.david.academysystem.dto.exercicios;

import jakarta.validation.constraints.NotBlank;

public record ExerciciosRequestDTO(
        @NotBlank(message = "O nome do exercicio não pode ser vazio.")
        String nome,
        @NotBlank(message = "O nome do grupo muscular do exercicio não pode ser vazio.")
        String grupoMuscular
) {
}
