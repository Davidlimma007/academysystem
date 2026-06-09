package com.david.academysystem.service;

import com.david.academysystem.database.repository.IExerciciosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Exercicios {

    private final IExerciciosRepository exerciciosRepository;

    public List<Exercicios> findAll(){
        return exerciciosRepository.findAll();
    }
}
