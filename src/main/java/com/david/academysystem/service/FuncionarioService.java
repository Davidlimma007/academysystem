package com.david.academysystem.service;

import com.david.academysystem.database.model.Funcionario;
import com.david.academysystem.database.model.Usuario;
import com.david.academysystem.database.repository.IFuncionarioRepository;
import com.david.academysystem.database.repository.IUsuarioRepository;
import com.david.academysystem.dto.funcionario.FuncionarioRequestDTO;
import com.david.academysystem.dto.funcionario.FuncionarioResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final IFuncionarioRepository funcionarioRepository;
    private final IUsuarioRepository usuarioRepository;

    @Transactional
    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (funcionarioRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new BadRequestException("Perfil de funcionário já cadastrado para este usuário.");
        }

        Funcionario funcionario = Funcionario.builder()
                .nome(dto.nome())
                .usuario(usuario)
                .build();

        funcionarioRepository.save(funcionario);
        return toResponseDTO(funcionario);
    }

    public List<FuncionarioResponseDTO> findAll() {
        return funcionarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public FuncionarioResponseDTO findById(UUID id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado."));
        return toResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO update(UUID id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado."));
        funcionario.setNome(dto.nome());
        funcionarioRepository.save(funcionario);
        return toResponseDTO(funcionario);
    }

    @Transactional
    public void delete(UUID id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new NotFoundException("Funcionário não encontrado.");
        }
        funcionarioRepository.deleteById(id);
    }

    public boolean isOwner(UUID id, String email) {
        return funcionarioRepository.existsByIdAndUsuarioEmail(id, email);
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario f) {
        return new FuncionarioResponseDTO(f.getId(), f.getNome(), f.getUsuario().getEmail());
    }
}
