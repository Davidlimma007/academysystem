package com.david.academysystem.controller;

import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasProjection;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasRequestDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
import com.david.academysystem.exception.ErrorResponse;
import com.david.academysystem.service.AvaliacoesFisicasService;
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
@RequestMapping("v1/avaliacoesfisicas")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
@Tag(name = "Avaliações Físicas", description = "Gerenciamento de avaliações físicas dos alunos")
@SecurityRequirement(name = "jwt_auth")
public class AvaliacoesFisicasController {

    private final AvaliacoesFisicasService avaliacoesFisicasService;

    @Operation(summary = "Criar avaliação física", description = "Registra uma nova avaliação física para um aluno. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AvaliacoesFisicasResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AvaliacoesFisicasResponseDTO> criarAvaliacoesFisicas(@Valid @RequestBody AvaliacoesFisicasRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoesFisicasService.criarAvaliacaoFisica(dto));
    }

    @Operation(summary = "Listar avaliações físicas", description = "Retorna a lista completa de avaliações físicas com nome do aluno. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<AvaliacoesFisicasProjection>> getAllAvaliacoes() {
        return ResponseEntity.ok(avaliacoesFisicasService.getAllAvaliacoes());
    }

    @Operation(summary = "Listar avaliações físicas paginado", description = "Retorna as avaliações físicas de forma paginada. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<Page<AvaliacoesFisicasProjection>> getAllAvaliacoesPage(@PathVariable Integer page,
                                                                                  @PathVariable Integer size) {
        return ResponseEntity.ok(avaliacoesFisicasService.getAllAvaliacoesPage(page, size));
    }

    @Operation(summary = "Buscar avaliação física por ID", description = "Retorna os dados de uma avaliação física específica. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AvaliacoesFisicasResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacoesFisicasResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(avaliacoesFisicasService.findById(id));
    }

    @Operation(summary = "Atualizar avaliação física", description = "Atualiza os dados de uma avaliação física existente. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AvaliacoesFisicasResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacoesFisicasResponseDTO> update(@PathVariable UUID id,
                                                               @Valid @RequestBody AvaliacoesFisicasRequestDTO dto) {
        return ResponseEntity.ok(avaliacoesFisicasService.update(id, dto));
    }

    @Operation(summary = "Excluir avaliação física", description = "Remove uma avaliação física. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avaliação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        avaliacoesFisicasService.delete(id);
    }
}
