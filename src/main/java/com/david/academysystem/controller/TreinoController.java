package com.david.academysystem.controller;

import com.david.academysystem.dto.treino.TreinoRequestDTO;
import com.david.academysystem.dto.treino.TreinoResponseDTO;
import com.david.academysystem.exception.ErrorResponse;
import com.david.academysystem.service.TreinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/treinos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Treinos", description = "Gerenciamento dos treinos dos alunos")
@SecurityRequirement(name = "jwt_auth")
public class TreinoController {

    private final TreinoService treinoService;

    @Operation(summary = "Criar treino", description = "Cria um novo treino para um aluno com os exercícios selecionados. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Treino criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TreinoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno ou exercício não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<TreinoResponseDTO> criarTreino(@Valid @RequestBody TreinoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinoService.criarTreino(dto));
    }

    @Operation(summary = "Listar treinos", description = "Retorna a lista de todos os treinos cadastrados. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<List<TreinoResponseDTO>> findAll() {
        return ResponseEntity.ok(treinoService.findAll());
    }

    @Operation(summary = "Listar treinos paginado", description = "Retorna os treinos de forma paginada. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/page/{page}/size/{size}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<Page<TreinoResponseDTO>> findAllPage(@PathVariable Integer page,
                                                               @PathVariable Integer size) {
        return ResponseEntity.ok(treinoService.findAllPage(page, size));
    }

    @Operation(summary = "Buscar treino por ID", description = "Retorna os dados de um treino específico. O aluno dono do treino pode acessá-lo; FUNCIONARIO e ADMIN têm acesso irrestrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TreinoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("@treinoService.pertenceAoAluno(#id, authentication.name) or hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<TreinoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(treinoService.findById(id));
    }

    @Operation(summary = "Atualizar treino", description = "Atualiza os dados de um treino existente. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TreinoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<TreinoResponseDTO> update(@PathVariable UUID id,
                                                    @Valid @RequestBody TreinoRequestDTO dto) {
        return ResponseEntity.ok(treinoService.update(id, dto));
    }

    @Operation(summary = "Excluir treino", description = "Remove um treino. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Treino excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        treinoService.delete(id);
    }
}
