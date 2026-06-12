package com.david.academysystem.controller;

import com.david.academysystem.dto.aluno.AlunoRequestDTO;
import com.david.academysystem.dto.aluno.AlunoResponseDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
import com.david.academysystem.service.AlunoService;
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
@RequestMapping("v1/alunos")
@RequiredArgsConstructor
@Validated
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<AlunoResponseDTO> criarAluno(@Valid @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criarAluno(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<List<AlunoResponseDTO>> findAll() {
        return ResponseEntity.ok(alunoService.listaGeral());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@alunoService.isOwner(#id, authentication.name) or hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<AlunoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@alunoService.isOwner(#id, authentication.name) or hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<AlunoResponseDTO> update(@PathVariable UUID id,
                                                   @Valid @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(alunoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        alunoService.delete(id);
    }

    @GetMapping("/{alunoId}/avaliacao")
    @PreAuthorize("@alunoService.isOwner(#alunoId, authentication.name) or hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public ResponseEntity<AvaliacoesFisicasResponseDTO> getAvaliacaoFisica(@PathVariable UUID alunoId) {
        return ResponseEntity.ok(alunoService.getAlunoAvaliacao(alunoId));
    }
}
