package com.david.academysystem.dto.treino;

import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;

import java.util.List;
import java.util.UUID;

public record TreinoResponseDTO(
        UUID id,
        String nome,
        UUID alunoId,
        List<ExerciciosResponseDTO> exercicios
) {
}
