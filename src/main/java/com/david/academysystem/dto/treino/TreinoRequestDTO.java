package com.david.academysystem.dto.treino;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TreinoRequestDTO(
        @NotNull(message = "O aluno deve ser informado.")
        UUID alunoId,
        @NotBlank(message = "O nome do treino deve ser informado.")
        String nome,
        @NotEmpty(message = "Os exercícios do aluno devem ser informados.")
        List<UUID> exerciciosIds
) {
}
