package com.david.academysystem.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Resposta de erro retornada pela API")
public class ErrorResponse {

    @Schema(description = "Código HTTP do erro", example = "404")
    private int status;

    @Schema(description = "Mensagem descritiva do erro", example = "Aluno não encontrado.")
    private String message;
}
