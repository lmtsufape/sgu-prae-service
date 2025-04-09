package br.edu.ufape.sguPraeService.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoAtendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalTime tempoAtendimento;

    @ElementCollection
    private List<LocalTime> horarios = new ArrayList<>();


}
