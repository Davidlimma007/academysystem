package com.david.academysystem.dto.valiacoesfisicas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados para criação ou atualização de avaliação física")
public record AvaliacoesFisicasRequestDTO(
        @Schema(description = "ID do aluno a ser avaliado", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "O Id do aluno deve ser passado.")
        UUID alunoId,

        @Schema(description = "Peso do aluno em quilogramas", example = "80.5")
        @NotNull(message = "O peso do aluno deve ser passado.")
        BigDecimal peso,

        @Schema(description = "Altura do aluno em metros", example = "1.75")
        @NotNull(message = "A altura do aluno deve ser passado.")
        BigDecimal altura,

        @Schema(description = "Porcentagem de gordura corporal", example = "18.5")
        @NotNull(message = "a porcentagem de gordura corporal do aluno deve ser passado.")
        BigDecimal porcentagemGorduraCorporal
) {
}
