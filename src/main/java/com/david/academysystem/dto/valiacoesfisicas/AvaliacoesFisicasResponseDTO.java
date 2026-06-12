package com.david.academysystem.dto.valiacoesfisicas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados da avaliação física retornados pela API")
public record AvaliacoesFisicasResponseDTO(
        @Schema(description = "ID da avaliação física", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "ID do aluno avaliado", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID alunoId,

        @Schema(description = "Peso do aluno em quilogramas", example = "80.5")
        BigDecimal peso,

        @Schema(description = "Altura do aluno em metros", example = "1.75")
        BigDecimal altura,

        @Schema(description = "Porcentagem de gordura corporal", example = "18.5")
        BigDecimal porcentagemGorduraCorporal
) {
}
