package br.edu.ufape.sguPraeService.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    boolean ativo = true;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    private Profissional profissional;

    @ManyToOne
    private TipoAtendimento tipoAtendimento;

    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vaga> vagas = new ArrayList<>();


    public void trocarVagas(List<Vaga> novas) {
        this.vagas.clear();
        novas.forEach(v -> {
            v.setCronograma(this);
            this.vagas.add(v);
        });
    }
}
