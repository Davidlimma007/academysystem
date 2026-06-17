package com.david.academysystem.service;

import com.david.academysystem.database.model.Aluno;
import com.david.academysystem.database.model.AvaliacoesFisicas;
import com.david.academysystem.database.repository.IAlunoRepository;
import com.david.academysystem.database.repository.IAvaliacoesFisicasRepository;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasProjection;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasRequestDTO;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacoesFisicasService {

    private final IAlunoRepository alunoRepository;
    private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;

    @Transactional
    public AvaliacoesFisicasResponseDTO criarAvaliacaoFisica(AvaliacoesFisicasRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        if (aluno.getAvaliacoesFisicas() != null) {
            throw new BadRequestException("Avaliação física já cadastrada para esse aluno.");
        }

        AvaliacoesFisicas avaliacao = AvaliacoesFisicas.builder()
                .peso(dto.peso())
                .altura(dto.altura())
                .porcentagemGorduraCorporal(dto.porcentagemGorduraCorporal())
                .build();

        aluno.setAvaliacoesFisicas(avaliacao);
        alunoRepository.save(aluno);

        return new AvaliacoesFisicasResponseDTO(
                aluno.getAvaliacoesFisicas().getId(),
                aluno.getId(),
                avaliacao.getPeso(),
                avaliacao.getAltura(),
                avaliacao.getPorcentagemGorduraCorporal()
        );
    }

    public List<AvaliacoesFisicasProjection> getAllAvaliacoes() {
        return avaliacoesFisicasRepository.getAllAvaliacoes();
    }

    public Page<AvaliacoesFisicasProjection> getAllAvaliacoesPage(Integer page, Integer size) {
        return avaliacoesFisicasRepository.getAllAvaliacoesPage(PageRequest.of(page, size));
    }

    public AvaliacoesFisicasResponseDTO findById(UUID id) {
        AvaliacoesFisicas avaliacao = avaliacoesFisicasRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avaliação física não encontrada."));
        Aluno aluno = alunoRepository.findByAvaliacoesFisicasId(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado para essa avaliação."));
        return toResponseDTO(avaliacao, aluno.getId());
    }

    @Transactional
    public AvaliacoesFisicasResponseDTO update(UUID id, AvaliacoesFisicasRequestDTO dto) {
        AvaliacoesFisicas avaliacao = avaliacoesFisicasRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avaliação física não encontrada."));
        Aluno aluno = alunoRepository.findByAvaliacoesFisicasId(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado para essa avaliação."));

        avaliacao.setPeso(dto.peso());
        avaliacao.setAltura(dto.altura());
        avaliacao.setPorcentagemGorduraCorporal(dto.porcentagemGorduraCorporal());
        avaliacoesFisicasRepository.save(avaliacao);

        return toResponseDTO(avaliacao, aluno.getId());
    }

    @Transactional
    public void delete(UUID id) {
        AvaliacoesFisicas avaliacao = avaliacoesFisicasRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avaliação física não encontrada."));
        Aluno aluno = alunoRepository.findByAvaliacoesFisicasId(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado para essa avaliação."));
        aluno.setAvaliacoesFisicas(null);
        alunoRepository.save(aluno);
    }

    private AvaliacoesFisicasResponseDTO toResponseDTO(AvaliacoesFisicas avaliacao, UUID alunoId) {
        return new AvaliacoesFisicasResponseDTO(
                avaliacao.getId(),
                alunoId,
                avaliacao.getPeso(),
                avaliacao.getAltura(),
                avaliacao.getPorcentagemGorduraCorporal()
        );
    }
}
