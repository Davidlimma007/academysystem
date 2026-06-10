package com.david.academysystem.controller;

import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasProjection;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasRequestDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
import com.david.academysystem.service.AvaliacoesFisicasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/avaliacoesfisicas")
@RequiredArgsConstructor
@Validated
public class AvaliacoesFisicasController {

    private final AvaliacoesFisicasService avaliacoesFisicasService;

    @PostMapping
    public ResponseEntity<AvaliacoesFisicasResponseDTO> criarAvaliacoesFisicas(@Valid @RequestBody AvaliacoesFisicasRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoesFisicasService.criarAvaliacaoFisica(dto));
    }

    @GetMapping
    public ResponseEntity<List<AvaliacoesFisicasProjection>> getAllAvaliacoes() {
        return ResponseEntity.ok(avaliacoesFisicasService.getAllAvaliacoes());
    }

    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<Page<AvaliacoesFisicasProjection>> getAllAvaliacoesPage(@PathVariable Integer page,
                                                                                  @PathVariable Integer size) {
        return ResponseEntity.ok(avaliacoesFisicasService.getAllAvaliacoesPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacoesFisicasResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(avaliacoesFisicasService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacoesFisicasResponseDTO> update(@PathVariable UUID id,
                                                               @Valid @RequestBody AvaliacoesFisicasRequestDTO dto) {
        return ResponseEntity.ok(avaliacoesFisicasService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        avaliacoesFisicasService.delete(id);
    }
}
