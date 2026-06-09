package com.david.academysystem.dto.exercicios;

import java.util.UUID;

public record ExerciciosResponseDTO(
        UUID id,
        String nome,
        String grupoMuscular
) {
}
