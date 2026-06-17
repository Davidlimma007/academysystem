package com.david.academysystem.service;

import com.david.academysystem.database.model.Aluno;
import com.david.academysystem.database.model.Exercicios;
import com.david.academysystem.database.model.Treinos;
import com.david.academysystem.database.repository.IAlunoRepository;
import com.david.academysystem.database.repository.IExerciciosRepository;
import com.david.academysystem.database.repository.ITreinosRepository;
import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import com.david.academysystem.dto.treino.TreinoRequestDTO;
import com.david.academysystem.dto.treino.TreinoResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TreinoService {

    private final ITreinosRepository treinosRepository;
    private final IAlunoRepository alunoRepository;
    private final IExerciciosRepository exerciciosRepository;

    @Transactional
    public TreinoResponseDTO criarTreino(TreinoRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        if (treinosRepository.findByNomeAndAlunoId(dto.nome(), dto.alunoId()).isPresent()) {
            throw new BadRequestException("Treino já existente para este aluno.");
        }

        Treinos treino = Treinos.builder()
                .nome(dto.nome())
                .aluno(aluno)
                .exercicios(resolveExercicios(dto.exerciciosIds()))
                .build();

        treinosRepository.save(treino);
        return toResponseDTO(treino);
    }

    public List<TreinoResponseDTO> findAll() {
        return treinosRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TreinoResponseDTO findById(UUID id) {
        Treinos treino = treinosRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado."));
        return toResponseDTO(treino);
    }

    @Transactional
    public TreinoResponseDTO update(UUID id, TreinoRequestDTO dto) {
        Treinos treino = treinosRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado."));
        treino.setNome(dto.nome());
        treino.setExercicios(resolveExercicios(dto.exerciciosIds()));
        treinosRepository.save(treino);
        return toResponseDTO(treino);
    }

    @Transactional
    public void delete(UUID id) {
        if (!treinosRepository.existsById(id)) {
            throw new NotFoundException("Treino não encontrado.");
        }
        treinosRepository.deleteById(id);
    }

    public boolean pertenceAoAluno(UUID treinoId, String email) {
        return treinosRepository.existsByIdAndAlunoUsuarioEmail(treinoId, email);
    }

    private Set<Exercicios> resolveExercicios(List<UUID> ids) {
        Set<Exercicios> exercicios = new HashSet<>();
        for (UUID exercicioId : ids) {
            Exercicios exercicio = exerciciosRepository.findById(exercicioId)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Exercício %s não encontrado.", exercicioId)));
            exercicios.add(exercicio);
        }
        return exercicios;
    }

    private TreinoResponseDTO toResponseDTO(Treinos treino) {
        List<ExerciciosResponseDTO> exerciciosDTOs = treino.getExercicios()
                .stream()
                .map(e -> new ExerciciosResponseDTO(e.getId(), e.getNome(), e.getGrupoMuscular()))
                .toList();
        return new TreinoResponseDTO(
                treino.getId(),
                treino.getNome(),
                treino.getAluno().getId(),
                exerciciosDTOs
        );
    }
}
