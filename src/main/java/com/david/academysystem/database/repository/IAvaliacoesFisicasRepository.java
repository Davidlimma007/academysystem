package com.david.academysystem.database.repository;

import com.david.academysystem.database.model.AvaliacoesFisicas;
import com.david.academysystem.dto.valiacoesfisicas.AvaliacoesFisicasProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.UUID;

public interface IAvaliacoesFisicasRepository extends JpaRepository<AvaliacoesFisicas, UUID> {

    @NativeQuery(value = """
            SELECT a.id                             idAluno,
                   a.nome                           nomeAluno,
                   af.id                            idAvaliacao,
                   af.peso                          peso,
                   af.altura                        altura,
                   af.porcentagem_gordura_corporal   porcentagemGorduraCorporal
            FROM avaliacoes_fisicas af
            INNER JOIN alunos a
            ON a.avaliacao_fisica_id = af.id
            """)
    List<AvaliacoesFisicasProjection> getAllAvaliacoes();

    @NativeQuery(value = """
            SELECT a.id                             idAluno,
                   a.nome                           nomeAluno,
                   af.id                            idAvaliacao,
                   af.peso                          peso,
                   af.altura                        altura,
                   af.porcentagem_gordura_corporal   porcentagemGorduraCorporal
            FROM avaliacoes_fisicas af
            INNER JOIN alunos a
            ON a.avaliacao_fisica_id = af.id
            """,
    countQuery = """
            SELECT COUNT(*)
            FROM avaliacoes_fisicas af
            INNER JOIN alunos a
            ON a.avaliacao_fisica_id = af.id
            """)
    Page<AvaliacoesFisicasProjection> getAllAvaliacoesPage(Pageable pageable);
}
