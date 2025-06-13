package br.edu.ufape.sguPraeService.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime horaInicio;

    private LocalTime horaFim;

    private boolean disponivel = true;


    //Evitar o Race Condition
    @Version
    private Long version;

    @ManyToOne
    @JsonBackReference
    private Cronograma cronograma;

}
