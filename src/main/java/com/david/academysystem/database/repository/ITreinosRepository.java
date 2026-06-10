package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Treinos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITreinosRepository extends JpaRepository<Treinos, UUID> {

    Optional<Treinos> findByNomeAndAlunoId(String nome, UUID alunoId);
}
