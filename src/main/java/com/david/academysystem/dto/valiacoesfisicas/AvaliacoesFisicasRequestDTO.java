package com.david.academysystem.dto.valiacoesfisicas;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AvaliacoesFisicasRequestDTO(
        @NotNull(message = "O Id do aluno deve ser passado.")
        UUID alunoId,
        @NotNull(message = "O peso do aluno deve ser passado.")
        BigDecimal peso,
        @NotNull(message = "A altura do aluno deve ser passado.")
        BigDecimal altura,
        @NotNull(message = "a porcentagem de gordura corporal do aluno deve ser passado.")
        BigDecimal porcentagemGorduraCorporal
) {
}
