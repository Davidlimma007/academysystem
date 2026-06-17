package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Exercicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IExerciciosRepository extends JpaRepository<Exercicios, UUID> {

    List<Exercicios> findAllByGrupoMuscular(String grupoMuscular);

    @Query(value = """
            SELECT e
            FROM Exercicios e
            WHERE UPPER(e.grupoMuscular) = UPPER(:grupoMuscular)
            """)
    List<Exercicios> findAllByGrupoMuscularJpql(@Param("grupoMuscular") String grupoMuscular);

    @NativeQuery(value = """
            SELECT id, nome, grupo_muscular
            FROM exercicios
            WHERE UPPER(grupo_muscular) = UPPER(:grupoMuscular)
            """)
    List<Exercicios> findAllByGrupoMuscularNativeQuery(@Param("grupoMuscular") String grupoMuscular);
}
