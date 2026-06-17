package com.david.academysystem.controller;

import com.david.academysystem.dto.exercicios.ExerciciosRequestDTO;
import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import com.david.academysystem.exception.ErrorResponse;
import com.david.academysystem.service.ExerciciosService;
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
@RequestMapping("v1/exercicios")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
@Tag(name = "Exercícios", description = "Gerenciamento do catálogo de exercícios")
@SecurityRequirement(name = "jwt_auth")
public class ExerciciosController {

    private final ExerciciosService exerciciosService;

    @Operation(summary = "Criar exercício", description = "Adiciona um novo exercício ao catálogo. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExerciciosResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ExerciciosResponseDTO> save(@Valid @RequestBody ExerciciosRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciciosService.save(dto));
    }

    @Operation(summary = "Listar exercícios", description = "Retorna todos os exercícios do catálogo. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ExerciciosResponseDTO>> findAll() {
        return ResponseEntity.ok(exerciciosService.findAll());
    }

    @Operation(summary = "Listar exercícios paginado", description = "Retorna os exercícios do catálogo de forma paginada. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<Page<ExerciciosResponseDTO>> findAllPage(@PathVariable Integer page,
                                                                   @PathVariable Integer size) {
        return ResponseEntity.ok(exerciciosService.findAllPage(page, size));
    }

    @Operation(summary = "Buscar exercício por ID", description = "Retorna os dados de um exercício específico. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercício encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExerciciosResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciciosResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(exerciciosService.findById(id));
    }

    @Operation(summary = "Buscar exercícios por grupo muscular", description = "Filtra exercícios pelo grupo muscular informado. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista filtrada retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/grupos/{grupoMuscular}")
    public ResponseEntity<List<ExerciciosResponseDTO>> getByGrupoMuscular(@PathVariable String grupoMuscular) {
        return ResponseEntity.ok(exerciciosService.getExercicioByGrupoMuscular(grupoMuscular));
    }

    @Operation(summary = "Atualizar exercício", description = "Atualiza os dados de um exercício existente. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExerciciosResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciciosResponseDTO> update(@PathVariable UUID id,
                                                        @Valid @RequestBody ExerciciosRequestDTO dto) {
        return ResponseEntity.ok(exerciciosService.update(id, dto));
    }

    @Operation(summary = "Excluir exercício", description = "Remove um exercício do catálogo. Requer role FUNCIONARIO ou ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercício excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        exerciciosService.delete(id);
    }
}
