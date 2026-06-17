package com.david.academysystem.service;

import com.david.academysystem.database.model.Exercicios;
import com.david.academysystem.database.repository.IExerciciosRepository;
import com.david.academysystem.dto.exercicios.ExerciciosRequestDTO;
import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import com.david.academysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciciosService {

    private final IExerciciosRepository exerciciosRepository;

    @Transactional
    public ExerciciosResponseDTO save(ExerciciosRequestDTO dto) {
        Exercicios exercicio = Exercicios.builder()
                .nome(dto.nome())
                .grupoMuscular(dto.grupoMuscular())
                .build();
        exerciciosRepository.save(exercicio);
        return toResponseDTO(exercicio);
    }

    public List<ExerciciosResponseDTO> findAll() {
        return exerciciosRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ExerciciosResponseDTO findById(UUID id) {
        Exercicios exercicio = exerciciosRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercício não encontrado."));
        return toResponseDTO(exercicio);
    }

    public List<ExerciciosResponseDTO> getExercicioByGrupoMuscular(String grupoMuscular) {
        return exerciciosRepository.findAllByGrupoMuscular(grupoMuscular)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public ExerciciosResponseDTO update(UUID id, ExerciciosRequestDTO dto) {
        Exercicios exercicio = exerciciosRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercício não encontrado."));
        exercicio.setNome(dto.nome());
        exercicio.setGrupoMuscular(dto.grupoMuscular());
        exerciciosRepository.save(exercicio);
        return toResponseDTO(exercicio);
    }

    @Transactional
    public void delete(UUID id) {
        if (!exerciciosRepository.existsById(id)) {
            throw new NotFoundException("Exercício não encontrado.");
        }
        exerciciosRepository.deleteById(id);
    }

    private ExerciciosResponseDTO toResponseDTO(Exercicios e) {
        return new ExerciciosResponseDTO(e.getId(), e.getNome(), e.getGrupoMuscular());
    }
}
