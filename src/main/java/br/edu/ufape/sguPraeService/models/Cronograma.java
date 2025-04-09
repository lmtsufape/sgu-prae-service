package br.edu.ufape.sguPraeService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    private Profissional profissional;

    @ManyToOne
    private TipoAtendimento tipoAtendimento;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaga> vagas;
}
