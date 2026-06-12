package com.david.academysystem.dto.token;

public record TokenResponseDTO(
        String token,
        long expiresIn
) {
}
