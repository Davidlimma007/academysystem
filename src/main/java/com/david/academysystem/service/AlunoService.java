package com.david.academysystem.service;

import com.david.academysystem.database.model.Aluno;
import com.david.academysystem.database.model.AvaliacoesFisicas;
import com.david.academysystem.database.repository.IAlunoRepository;
import com.david.academysystem.dto.aluno.AlunoRequestDTO;
import com.david.academysystem.dto.aluno.AlunoResponseDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
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

    @Transactional
    public AlunoResponseDTO criarAluno(AlunoRequestDTO dto) {
        Aluno aluno = Aluno.builder()
                .nome(dto.nome())
                .build();
        alunoRepository.save(aluno);
        return new AlunoResponseDTO(aluno.getId(), aluno.getNome());
    }

    public List<AlunoResponseDTO> listaGeral() {
        return alunoRepository.findAll()
                .stream()
                .map(a -> new AlunoResponseDTO(a.getId(), a.getNome()))
                .toList();
    }

    public AlunoResponseDTO findById(UUID id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        return new AlunoResponseDTO(aluno.getId(), aluno.getNome());
    }

    @Transactional
    public AlunoResponseDTO update(UUID id, AlunoRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        aluno.setNome(dto.nome());
        alunoRepository.save(aluno);
        return new AlunoResponseDTO(aluno.getId(), aluno.getNome());
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
}
