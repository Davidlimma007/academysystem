package com.david.academysystem.dto.aluno;

import java.util.UUID;

public record AlunoResponseDTO(
        UUID id,
        String nome
) {
}
