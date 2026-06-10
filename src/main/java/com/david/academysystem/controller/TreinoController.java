package com.david.academysystem.controller;

import com.david.academysystem.dto.treino.TreinoRequestDTO;
import com.david.academysystem.dto.treino.TreinoResponseDTO;
import com.david.academysystem.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/treinos")
@RequiredArgsConstructor
@Validated
public class TreinoController {

    private final TreinoService treinoService;

    @PostMapping
    public ResponseEntity<TreinoResponseDTO> criarTreino(@Valid @RequestBody TreinoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinoService.criarTreino(dto));
    }

    @GetMapping
    public ResponseEntity<List<TreinoResponseDTO>> findAll() {
        return ResponseEntity.ok(treinoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(treinoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> update(@PathVariable UUID id,
                                                    @Valid @RequestBody TreinoRequestDTO dto) {
        return ResponseEntity.ok(treinoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        treinoService.delete(id);
    }
}
