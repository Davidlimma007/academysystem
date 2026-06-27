package com.david.academysystem.dto.auth.cadastro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Dados para cadastro de um novo aluno")
public record AuthAlunoRequestDTO(
        @Schema(description = "E-mail do aluno", example = "joao@email.com")
        @NotBlank(message = "O e-mail deve ser informado.")
        String email,

        @Schema(description = "Senha de acesso (entre 10 e 100 caracteres)", example = "senha@12345")
        @NotBlank(message = "A senha deve ser informada.")
        @Length(min = 10, max = 100, message = "A senha deve conter entre (10) e (100) caracteres")
        String senha,

        @Schema(description = "Nome completo do aluno", example = "João da Silva")
        @NotBlank(message = "O nome deve ser informado.")
        String nome
) {
}
