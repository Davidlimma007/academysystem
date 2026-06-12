package com.david.academysystem.dto.funcionario;

import java.util.UUID;

public record FuncionarioResponseDTO(
        UUID id,
        String nome,
        String email
) {
}
