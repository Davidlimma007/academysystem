package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByNome(String nome);
}
