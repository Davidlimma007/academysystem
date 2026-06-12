package com.david.academysystem.controller;

import com.david.academysystem.dto.funcionario.FuncionarioRequestDTO;
import com.david.academysystem.dto.funcionario.FuncionarioResponseDTO;
import com.david.academysystem.service.FuncionarioService;
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
@RequestMapping("v1/funcionarios")
@RequiredArgsConstructor
@Validated
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> criar(@Valid @RequestBody FuncionarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioService.criar(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FuncionarioResponseDTO>> findAll() {
        return ResponseEntity.ok(funcionarioService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@funcionarioService.isOwner(#id, authentication.name) or hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(funcionarioService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@funcionarioService.isOwner(#id, authentication.name) or hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> update(@PathVariable UUID id,
                                                         @Valid @RequestBody FuncionarioRequestDTO dto) {
        return ResponseEntity.ok(funcionarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        funcionarioService.delete(id);
    }
}
