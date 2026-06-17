package com.david.academysystem.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "treinos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Treinos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "treinos_exercicios",
            joinColumns = @JoinColumn(name = "treino_id"),
            inverseJoinColumns = @JoinColumn(name = "exercicio_id")
    )
    private Set<Exercicios> exercicios = new HashSet<>();
}
