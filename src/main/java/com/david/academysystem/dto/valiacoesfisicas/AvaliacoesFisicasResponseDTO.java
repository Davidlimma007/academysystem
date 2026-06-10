package com.david.academysystem.dto.valiacoesfisicas;

import java.math.BigDecimal;
import java.util.UUID;

public record AvaliacoesFisicasResponseDTO(
        UUID id,
        UUID alunoId,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal porcentagemGorduraCorporal
) {
}
