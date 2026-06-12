package com.david.academysystem.service;

import com.david.academysystem.database.model.Aluno;
import com.david.academysystem.database.model.AvaliacoesFisicas;
import com.david.academysystem.database.model.Usuario;
import com.david.academysystem.database.repository.IAlunoRepository;
import com.david.academysystem.database.repository.IUsuarioRepository;
import com.david.academysystem.dto.aluno.AlunoRequestDTO;
import com.david.academysystem.dto.aluno.AlunoResponseDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final IAlunoRepository alunoRepository;
    private final IUsuarioRepository usuarioRepository;

    @Transactional
    public AlunoResponseDTO criarAluno(AlunoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (alunoRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new BadRequestException("Perfil de aluno já cadastrado para este usuário.");
        }

        Aluno aluno = Aluno.builder()
                .nome(dto.nome())
                .usuario(usuario)
                .build();

        alunoRepository.save(aluno);
        return toResponseDTO(aluno);
    }

    public List<AlunoResponseDTO> listaGeral() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AlunoResponseDTO findById(UUID id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        return toResponseDTO(aluno);
    }

    @Transactional
    public AlunoResponseDTO update(UUID id, AlunoRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        aluno.setNome(dto.nome());
        alunoRepository.save(aluno);
        return toResponseDTO(aluno);
    }

    @Transactional
    public void delete(UUID id) {
        if (!alunoRepository.existsById(id)) {
            throw new NotFoundException("Aluno não encontrado.");
        }
        alunoRepository.deleteById(id);
    }

    public AvaliacoesFisicasResponseDTO getAlunoAvaliacao(UUID alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        AvaliacoesFisicas avaliacao = aluno.getAvaliacoesFisicas();
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação física não encontrada para esse aluno.");
        }
        return new AvaliacoesFisicasResponseDTO(
                avaliacao.getId(),
                aluno.getId(),
                avaliacao.getPeso(),
                avaliacao.getAltura(),
                avaliacao.getPorcentagemGorduraCorporal()
        );
    }

    public boolean isOwner(UUID alunoId, String email) {
        return alunoRepository.existsByIdAndUsuarioEmail(alunoId, email);
    }

    private AlunoResponseDTO toResponseDTO(Aluno aluno) {
        String email = aluno.getUsuario() != null ? aluno.getUsuario().getEmail() : null;
        return new AlunoResponseDTO(aluno.getId(), aluno.getNome(), email);
    }
}
