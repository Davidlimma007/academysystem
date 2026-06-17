package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IFuncionarioRepository extends JpaRepository<Funcionario, UUID> {

    Optional<Funcionario> findByUsuarioEmail(String email);

    boolean existsByIdAndUsuarioEmail(UUID id, String email);

    boolean existsByUsuarioId(UUID usuarioId);
}
