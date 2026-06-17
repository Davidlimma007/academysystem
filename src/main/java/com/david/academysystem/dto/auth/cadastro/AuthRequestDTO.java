package com.david.academysystem.dto.auth.cadastro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Dados para cadastro de um novo usuário")
public record AuthRequestDTO(
        @Schema(description = "E-mail do usuário", example = "joao@email.com")
        @NotBlank(message = "O e-mail deve ser informado.")
        String email,

        @Schema(description = "Senha de acesso (entre 10 e 100 caracteres)", example = "senha@12345")
        @NotBlank(message = "A senha deve ser informada.")
        @Length(min = 10, max = 100, message = "A senha deve conter entre (5) e (100) caracteres")
        String senha
) {
}
