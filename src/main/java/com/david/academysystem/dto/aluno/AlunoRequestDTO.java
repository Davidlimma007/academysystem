package com.david.academysystem.dto.aluno;

import jakarta.validation.constraints.NotBlank;

public record AlunoRequestDTO(
        @NotBlank(message = "O nome do aluno deve ser informado.")
        String nome
) {
}
