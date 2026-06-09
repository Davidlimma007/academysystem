package com.david.academysystem.controller;

import com.david.academysystem.database.model.Exercicios;
import com.david.academysystem.dto.exercicios.ExerciciosRequestDTO;
import com.david.academysystem.service.ExerciciosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/exercicios")
@RequiredArgsConstructor
@Validated
public class ExerciciosController {

    private final ExerciciosService exerciciosService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Exercicios> findAll(){
        return exerciciosService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody ExerciciosRequestDTO dto){
        exerciciosService.save(dto);
    }

    @GetMapping("/grupos/{grupoMuscular}")
    @ResponseStatus(HttpStatus.OK)
    public List<Exercicios> getExercicioByGrupoMuscular(@PathVariable String grupoMuscular){
        return exerciciosService.getExercicioByGrupoMuscular(grupoMuscular);
    }
}
