package br.edu.ufape.sguPraeService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String especialidade;

    private Boolean ativo = true;
}
