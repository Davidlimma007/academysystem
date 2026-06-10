package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAlunoRepository extends JpaRepository<Aluno, UUID> {

    Optional<Aluno> findByAvaliacoesFisicasId(UUID avaliacaoId);
}
