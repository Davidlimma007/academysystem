package com.david.academysystem.controller;

import com.david.academysystem.dto.exercicios.ExerciciosRequestDTO;
import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import com.david.academysystem.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class ExerciciosController {

    private final ExerciciosService exerciciosService;

    @PostMapping
    public ResponseEntity<ExerciciosResponseDTO> save(@Valid @RequestBody ExerciciosRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciciosService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ExerciciosResponseDTO>> findAll() {
        return ResponseEntity.ok(exerciciosService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciciosResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(exerciciosService.findById(id));
    }

    @GetMapping("/grupos/{grupoMuscular}")
    public ResponseEntity<List<ExerciciosResponseDTO>> getByGrupoMuscular(@PathVariable String grupoMuscular) {
        return ResponseEntity.ok(exerciciosService.getExercicioByGrupoMuscular(grupoMuscular));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciciosResponseDTO> update(@PathVariable UUID id,
                                                        @Valid @RequestBody ExerciciosRequestDTO dto) {
        return ResponseEntity.ok(exerciciosService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        exerciciosService.delete(id);
    }
}
