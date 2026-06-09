package com.david.academysystem.service;

import com.david.academysystem.database.model.Exercicios;
import com.david.academysystem.database.repository.IExerciciosRepository;
import com.david.academysystem.dto.exercicios.ExerciciosRequestDTO;
import com.david.academysystem.dto.exercicios.ExerciciosResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciciosService {

    private final IExerciciosRepository exerciciosRepository;

    public List<Exercicios> findAll(){
        return exerciciosRepository.findAll();
    }

    public void save(ExerciciosRequestDTO dto){
        Exercicios exercicios = Exercicios.builder()
                        .nome(dto.nome())
                        .grupoMuscular(dto.grupoMuscular())
                        .build();

        exerciciosRepository.save(exercicios);
    }

    public List<Exercicios> getExercicioByGrupoMuscular(String grupoMuscular){
        return exerciciosRepository.findAllByGrupoMuscular(grupoMuscular);
    }
}
