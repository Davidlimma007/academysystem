package com.david.academysystem.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT gerado após autenticação bem-sucedida")
public record TokenResponseDTO(
        @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGVtYWlsLmNvbSJ9.abc123")
        String token,

        @Schema(description = "Tempo de expiração do token em milissegundos", example = "86400000")
        long expiresIn
) {
}
