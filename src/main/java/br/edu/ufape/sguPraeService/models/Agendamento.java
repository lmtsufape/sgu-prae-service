package br.edu.ufape.sguPraeService.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;


    private boolean ativo = true;

    @ManyToOne
    private Vaga vaga;

    @ManyToOne
    private Estudante estudante;
}
