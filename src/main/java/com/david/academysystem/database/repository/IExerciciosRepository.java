package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Exercicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IExerciciosRepository extends JpaRepository<Exercicios, UUID> {

    // Query Method
    List<Exercicios> findAllByGrupoMuscular(String grupoMuscular);


    // JPQL
    @Query(value = """
            SELECT e 
            FROM exercicios e 
            WHERE UPPER(e.grupoMuscular) = UPPER(:grupoMuscular)
            """)
    List<Exercicios> findAllByGrupoMuscularJpql(@Param("grupoMuscular") String grupoMuscular);


    // NATIVE QUERY   --- IMPORTANTE: O nome do campo deve ser o mesmo do banco de dados
    @NativeQuery(value = """
            SELECT e 
            FROM exercicios e 
            WHERE UPPER(e.grupo_muscular) = UPPER(:grupoMuscular)
            """)
    List<Exercicios> findAllByGrupoMuscularNativeQuery(@Param("grupoMuscular") String grupoMuscular);
}
